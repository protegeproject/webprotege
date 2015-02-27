package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewImpl;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.user.UserId;

public class WatchedEntitiesPortlet extends AbstractOWLEntityPortlet {

    private ChangeListView changeListView;

    public WatchedEntitiesPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setHeight(200);
        changeListView = new ChangeListViewImpl();
        ScrollPanel scrollPanel = new ScrollPanel(changeListView.asWidget());
        scrollPanel.setWidth("100%");
        scrollPanel.setHeight("100%");
        add(scrollPanel);
    }


    @Override
    public void onLogin(UserId userId) {
        onRefresh();
    }

    @Override
    public void onLogout(UserId userId) {
        onRefresh();
    }

    @Override
    protected void onRefresh() {
        ChangeListViewPresenter presenter = new ChangeListViewPresenter(changeListView, DispatchServiceManager.get());
        presenter.setChangesForWatches(getProjectId(), getUserId());
        setTitle(generateTitle());
    }

    private String generateTitle() {
        return "Watched Entities " + (Application.get().isGuestUser() ? " - Sign in to see the watched entities" : " for " + Application.get().getUserDisplayName());
    }
}
