package edu.stanford.bmir.protege.web.client.ui.ontology.individuals;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;

import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * A single view that shows the classes in an ontology.
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class IndividualsTab extends AbstractTab {

    @Inject
    public IndividualsTab(SelectionModel selectionModel, ProjectId projectId, ProjectManager projectManager) {
        super(selectionModel, projectId, projectManager);
    }
}
