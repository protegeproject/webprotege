package edu.stanford.bmir.protege.web.client.change;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedHandler;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

public class ChangesPortlet extends AbstractOWLEntityPortlet {

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
	public ChangesPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
		super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.dispatchServiceManager = dispatchServiceManager;

        changeListView = new ChangeListViewImpl();

        ScrollPanel scrollPanel = new ScrollPanel(changeListView.asWidget());
        getContentHolder().setWidget(scrollPanel);
        addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                ChangesPortlet.this.handleProjectChanged(event);
            }
        });
        addApplicationEventHandler(PermissionsChangedEvent.TYPE, new PermissionsChangedHandler() {
            @Override
            public void handlePersmissionsChanged(PermissionsChangedEvent event) {
                updateDisplayForSelectedEntity();
            }
        });
	}

	private ChangeListView changeListView;

    private void handleProjectChanged(ProjectChangedEvent event) {
        if(lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        lastRevisionNumber = event.getRevisionNumber();
        for(OWLEntityData entityData : event.getSubjects()) {
            if(isSelected(entityData.getEntity())) {
                updateDisplayForSelectedEntity();
                return;
            }
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        updateDisplayForSelectedEntity();
    }

    private void updateDisplayForSelectedEntity() {
        ProjectId projectId = getProjectId();
		if (getSelectedEntity().isPresent()) {
			ChangeListViewPresenter presenter = new ChangeListViewPresenter(changeListView, dispatchServiceManager);
			presenter.setChangesForEntity(projectId, getSelectedEntity().get());
		    setTitle("Changes for " + getSelectedEntityData().get().getBrowserText());
        }
        else {
            setTitle("Noting selected");
        }
	}

}
