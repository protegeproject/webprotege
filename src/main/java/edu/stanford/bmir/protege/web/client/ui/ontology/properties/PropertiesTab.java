package edu.stanford.bmir.protege.web.client.ui.ontology.properties;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;


public class PropertiesTab extends AbstractTab {

	public PropertiesTab(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, Project project) {
		super(selectionModel, eventBus, dispatchServiceManager, project);
	}

	public String getLabel() {
		return "Properties";
	}
}
