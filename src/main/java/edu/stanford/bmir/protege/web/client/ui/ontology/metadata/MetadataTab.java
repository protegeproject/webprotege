package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * A tab that shows metadata about an ontology such as the imported 
 * ontologies, comments, default language, version information, etc.
 * 
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MetadataTab extends AbstractTab {

	public MetadataTab(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, Project project) {
		super(selectionModel, eventBus, dispatchServiceManager, project);
	}
}
