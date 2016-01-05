package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * A user defined tab. The user can compose her own tab and fill it
 * with any of the available portlets.
 * The configuration of this tab is stored in theproject configuration,
 * so that it can be later retrieved when the user logs back in.
 * 
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class UserDefinedTab extends AbstractTab {

	@Inject
	public UserDefinedTab(@Assisted TabId tabId, SelectionModel selectionModel, ProjectId projectId, ProjectManager projectManager) {
		super(tabId, selectionModel, projectId, projectManager);
	}
}
