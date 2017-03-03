package edu.stanford.bmir.protege.web.client.ui.ontology.id;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
@Portlet(id = "portlets.OntologyId", title = "Ontology Id")
public class OntologyIdPortlet extends AbstractWebProtegePortlet implements HasFixedPrimaryAxisSize {

    private final DispatchServiceManager dispatchServiceManager;

    private OntologyIdView editor;

    @Inject
    public OntologyIdPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserManager loggedInUserManager) {
        super(selectionModel, eventBus, projectId);
        this.dispatchServiceManager = dispatchServiceManager;
        editor = new OntologyIdViewImpl();
        setWidget(editor.asWidget());
        setTitle("Ontology Id");
        editor.setEnabled(false);
        updateDisplay();
    }

    private void updateDisplay() {
        dispatchServiceManager.execute(new GetRootOntologyIdAction(getProjectId()),
                                       result -> editor.setValue(result.getObject()));
    }

    @Override
    public int getFixedPrimaryAxisSize() {
        return 80;
    }
}
