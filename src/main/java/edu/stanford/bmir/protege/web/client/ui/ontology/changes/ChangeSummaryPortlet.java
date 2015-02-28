package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewImpl;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;

import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

public class ChangeSummaryPortlet extends AbstractOWLEntityPortlet {

    public ChangeSummaryPortlet(Project project) {
        super(project);
    }

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

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
                ChangeSummaryPortlet.this.handleProjectChanged(event);
            }
        });
        onRefresh();
    }

    private void handleProjectChanged(ProjectChangedEvent event) {
        if(lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        lastRevisionNumber = event.getRevisionNumber();
        onRefresh();
    }

    @Override
    protected void onRefresh() {

        ProjectId projectId = getProjectId();
            ChangeListViewPresenter presenter = new ChangeListViewPresenter(changeListView, DispatchServiceManager.get());
            presenter.setChangesForProject(projectId);
            setTitle("Changes for project");
    }
}
