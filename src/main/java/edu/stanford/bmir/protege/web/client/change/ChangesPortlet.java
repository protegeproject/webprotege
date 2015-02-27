package edu.stanford.bmir.protege.web.client.change;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class ChangesPortlet extends AbstractOWLEntityPortlet {

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

	public ChangesPortlet(Project project) {
		super(project);
	}

	private ChangeListView changeListView;

	@Override
	public void initialize() {
		setHeight(200);
		changeListView = new ChangeListViewImpl();
        ScrollPanel scrollPanel = new ScrollPanel(changeListView.asWidget());
		scrollPanel.setWidth("100%");
		scrollPanel.setHeight("100%");
        add(scrollPanel);
        addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                ChangesPortlet.this.handleProjectChanged(event);
            }
        });
	}

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
			ChangeListViewPresenter presenter = new ChangeListViewPresenter(changeListView, DispatchServiceManager.get());
			presenter.setChangesForEntity(projectId, getSelectedEntity().get());
		    setTitle("Changes for " + getSelectedEntityData().get().getBrowserText());
        }
        else {
            setTitle("Noting selected");
        }
	}

}
