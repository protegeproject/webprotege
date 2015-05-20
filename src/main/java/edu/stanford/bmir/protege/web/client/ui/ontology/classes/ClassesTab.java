package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

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

	public ClassesTab(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

}
