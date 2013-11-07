package edu.stanford.bmir.protege.web.client.ui.ontology.annotations;

import com.google.common.base.Optional;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.shared.event.OntologyFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.OntologyFrameChangedEventHandler;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.Collection;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class OntologyAnnotationsPortlet extends AbstractEntityPortlet {

    private static final int DEFAULT_HEIGHT = 400;

    private AnnotationsView annotationsView;

    private boolean loaded = false;

    private Optional<Set<OWLAnnotation>> lastSet = Optional.absent();

    public OntologyAnnotationsPortlet(Project project) {
        super(project);
    }

    public OntologyAnnotationsPortlet(Project project, boolean initialize) {
        super(project, initialize);
    }

    @Override
    public Collection<EntityData> getSelection() {
        return null;
    }

    @Override
    public void reload() {
        if(!loaded) {
            loaded = true;
            onRefresh();
        }
    }

    @Override
    protected void onRefresh() {
        updateView();
    }

    private void updateView() {
        DispatchServiceManager.get().execute(new GetOntologyAnnotationsAction(getProjectId()), new AsyncCallback<RenderableGetObjectResult<Set<OWLAnnotation>>>() {
            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("There was a problem retrieving the annotation for this project.");
            }

            @Override
            public void onSuccess(RenderableGetObjectResult<Set<OWLAnnotation>> result) {
                final Set<OWLAnnotation> object = result.getObject();
                if(!annotationsView.getValue().equals(Optional.of(result.getObject()))) {
                    lastSet = Optional.of(object);
                    annotationsView.setValue(object);
                }
            }
        });
    }

    @Override
    public void initialize() {
        setTitle("Ontology annotations");
        annotationsView = new AnnotationsViewImpl(getProjectId());
        add(new ScrollPanel(annotationsView.asWidget()));
        annotationsView.addValueChangeHandler(new ValueChangeHandler<Optional<Set<OWLAnnotation>>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<Set<OWLAnnotation>>> event) {
                handleOntologyAnnotationsChanged();
            }
        });
        setHeight(DEFAULT_HEIGHT);
        addProjectEventHandler(OntologyFrameChangedEvent.TYPE, new OntologyFrameChangedEventHandler() {
            @Override
            public void ontologyFrameChanged(OntologyFrameChangedEvent event) {
                updateView();
            }
        });
        updateState();

    }

    private void updateState() {
        annotationsView.setEnabled(hasWritePermission());
    }

    @Override
    protected void onLogin(UserId userId) {
        updateState();
    }

    @Override
    protected void onLogout(UserId userId) {
        updateState();
    }

    @Override
    public void onPermissionsChanged() {
        updateState();
    }

    private void handleOntologyAnnotationsChanged() {
        if(!annotationsView.isDirty()) {
            return;
        }
        if(!annotationsView.isWellFormed()) {
            return;
        }
        Optional<Set<OWLAnnotation>> annotations = annotationsView.getValue();
        if (annotations.isPresent() && lastSet.isPresent()) {
            DispatchServiceManager.get().execute(new SetOntologyAnnotationsAction(getProjectId(), lastSet.get(), annotations.get()), new AsyncCallback<SetOntologyAnnotationsResult>() {
                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.alert("There was a problem setting the ontology annotations for this project.");
                    GWT.log("Problem setting ontology annotations", caught);
                }

                @Override
                public void onSuccess(SetOntologyAnnotationsResult result) {

                }
            });
        }
    }
}
