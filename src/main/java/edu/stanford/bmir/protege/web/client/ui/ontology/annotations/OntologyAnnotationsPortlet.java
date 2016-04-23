package edu.stanford.bmir.protege.web.client.ui.ontology.annotations;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.event.OntologyFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.OntologyFrameChangedEventHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.inject.Inject;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class OntologyAnnotationsPortlet extends AbstractWebProtegePortlet {

    private static final int DEFAULT_HEIGHT = 400;

    private final AnnotationsView annotationsView;

    private Optional<Set<OWLAnnotation>> lastSet = Optional.absent();

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public OntologyAnnotationsPortlet(AnnotationsView annotationsView, SelectionModel selectionModel, EventBus eventBus,  DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserProvider loggedInUserProvider, LoggedInUserProjectPermissionChecker permissionChecker) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        this.annotationsView = annotationsView;
        this.dispatchServiceManager = dispatchServiceManager;
        this.permissionChecker = permissionChecker;
        setTitle("Ontology annotations");
        setWidget(new ScrollPanel(annotationsView.asWidget()));
        annotationsView.addValueChangeHandler(new ValueChangeHandler<Optional<Set<OWLAnnotation>>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<Set<OWLAnnotation>>> event) {
                handleOntologyAnnotationsChanged();
            }
        });
        addProjectEventHandler(OntologyFrameChangedEvent.TYPE, new OntologyFrameChangedEventHandler() {
            @Override
            public void ontologyFrameChanged(OntologyFrameChangedEvent event) {
                updateView();
            }
        });
        updateState();
        updateView();
    }


    private void updateView() {
        dispatchServiceManager.execute(new GetOntologyAnnotationsAction(getProjectId()), new DispatchServiceCallback<RenderableGetObjectResult<Set<OWLAnnotation>>>() {
            @Override
            public void handleSuccess(RenderableGetObjectResult<Set<OWLAnnotation>> result) {
                final Set<OWLAnnotation> object = result.getObject();
                if (!lastSet.isPresent() || !annotationsView.getValue().equals(Optional.of(result.getObject()))) {
                    lastSet = Optional.of(object);
                    annotationsView.setValue(object);
                }
            }
        });
    }


    private void updateState() {
        annotationsView.setEnabled(false);
        permissionChecker.hasWritePermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean result) {
                annotationsView.setEnabled(result);
            }
        });
    }

    @Override
    protected void handleLogin(UserId userId) {
        updateState();
    }

    @Override
    protected void handleLogout(UserId userId) {
        updateState();
    }

    @Override
    public void handlePermissionsChanged() {
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
            dispatchServiceManager.execute(new SetOntologyAnnotationsAction(getProjectId(), lastSet.get(), annotations.get()), new DispatchServiceCallback<SetOntologyAnnotationsResult>() {
                @Override
                public void handleSuccess(SetOntologyAnnotationsResult result) {

                }
            });
        }
    }
}
