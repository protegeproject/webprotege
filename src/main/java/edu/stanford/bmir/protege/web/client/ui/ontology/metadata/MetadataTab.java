package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * A tab that shows metadata about an ontology such as the imported 
 * ontologies, comments, default language, version information, etc.
 * 
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MetadataTab extends AbstractTab {

	@Inject
	public MetadataTab(SelectionModel selectionModel, ProjectId projectId, ProjectManager projectManager) {
		super(selectionModel, projectId, projectManager);
	}
}
