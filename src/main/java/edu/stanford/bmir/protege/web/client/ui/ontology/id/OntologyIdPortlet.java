package edu.stanford.bmir.protege.web.client.ui.ontology.id;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class OntologyIdPortlet extends AbstractOWLEntityPortlet {

    private final DispatchServiceManager dispatchServiceManager;

    private OntologyIdEditor editor;

    public OntologyIdPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, Project project) {
        super(selectionModel, eventBus, project);
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void initialize() {
        editor = new OntologyIdViewImpl();
        add(editor.asWidget());
        setTitle("Ontology Id");
        editor.setEnabled(false);
        updateDisplay();
    }

    private void updateDisplay() {
        dispatchServiceManager.execute(new GetRootOntologyIdAction(getProjectId()), new DispatchServiceCallback<GetRootOntologyIdResult>() {

            @Override
            public void handleSuccess(GetRootOntologyIdResult result) {
                editor.setValue(result.getObject());
            }
        });
    }
}
