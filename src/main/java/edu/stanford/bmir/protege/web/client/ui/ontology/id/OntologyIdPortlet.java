package edu.stanford.bmir.protege.web.client.ui.ontology.id;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class OntologyIdPortlet extends AbstractOWLEntityPortlet {

    private final DispatchServiceManager dispatchServiceManager;

    private OntologyIdEditor editor;

    @Inject
    public OntologyIdPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserManager loggedInUserManager) {
        super(selectionModel, eventBus, projectId, loggedInUserManager);
        this.dispatchServiceManager = dispatchServiceManager;
        editor = new OntologyIdViewImpl();
        getContentHolder().setWidget(editor.asWidget());
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
