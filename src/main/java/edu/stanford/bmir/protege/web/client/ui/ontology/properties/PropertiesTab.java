package edu.stanford.bmir.protege.web.client.ui.ontology.properties;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;


public class PropertiesTab extends AbstractTab {

	public PropertiesTab(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	public String getLabel() {
		return "Properties";
	}
}
