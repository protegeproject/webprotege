package edu.stanford.bmir.protege.web.client.ui.ontology.annotations;

import com.google.common.base.Optional;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
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

    private AnnotationsView annotationsView;

    private boolean loaded = false;

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
        DispatchServiceManager.get().execute(new GetOntologyAnnotationsAction(getProjectId()), new AsyncCallback<GetObjectResult<Set<OWLAnnotation>>>() {
            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("There was a problem retrieving the annotation for this project.");
            }

            @Override
            public void onSuccess(GetObjectResult<Set<OWLAnnotation>> result) {
                annotationsView.setValue(result.getObject());
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
                GWT.log("Handle ontology annotations changed!");
            }
        });
        annotationsView.setEnabled(false);
    }
}
