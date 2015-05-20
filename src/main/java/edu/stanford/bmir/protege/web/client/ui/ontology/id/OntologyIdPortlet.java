package edu.stanford.bmir.protege.web.client.ui.ontology.id;

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

    private OntologyIdEditor editor;

    public OntologyIdPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
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
        DispatchServiceManager.get().execute(new GetRootOntologyIdAction(getProjectId()), new DispatchServiceCallback<GetRootOntologyIdResult>() {

            @Override
            public void handleSuccess(GetRootOntologyIdResult result) {
                editor.setValue(result.getObject());
            }
        });
    }
}
