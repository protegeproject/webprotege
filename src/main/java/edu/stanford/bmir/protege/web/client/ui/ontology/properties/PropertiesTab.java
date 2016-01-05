package edu.stanford.bmir.protege.web.client.ui.ontology.properties;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;


public class PropertiesTab extends AbstractTab {

	@Inject
	public PropertiesTab(SelectionModel selectionModel, ProjectId projectId, ProjectManager projectManager) {
		super(selectionModel, projectId, projectManager);
	}

	public String getLabel() {
		return "Properties";
	}
}
