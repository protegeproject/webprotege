package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * A single view that shows the classes in an ontology.
 * 
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class ClassesTab extends AbstractTab {

	public ClassesTab(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, Project project) {
		super(selectionModel, eventBus, dispatchServiceManager, project);
	}

}
