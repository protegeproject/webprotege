package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewPresenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.WATCH_CHANGES;

@Portlet(id = "portlets.WatchedEntities", title = "Watched Entities")
public class WatchedEntitiesPortlet extends AbstractWebProtegePortlet {

    private final LoggedInUserProvider loggedInUserProvider;

    private final ChangeListViewPresenter presenter;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final ForbiddenView forbiddenView;

    private final Timer refreshTimer = new Timer() {
        @Override
        public void run() {
            onRefresh();
        }
    };

    @Inject
    public WatchedEntitiesPortlet(ChangeListViewPresenter presenter,
                                  LoggedInUserProjectPermissionChecker permissionChecker,
                                  SelectionModel selectionModel, EventBus eventBus,
                                  ProjectId projectId,
                                  LoggedInUserProvider loggedInUserProvider,
                                  ForbiddenView forbiddenView) {
        super(selectionModel, eventBus, projectId);
        this.loggedInUserProvider = loggedInUserProvider;
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
        this.forbiddenView = forbiddenView;
        forbiddenView.setSubMessage("You do not have permission to watch changes in this project");
        ChangeListView changeListView = presenter.getView();
        setWidget(changeListView);
        onRefresh();
        addProjectEventHandler(WatchAddedEvent.TYPE, event -> refreshDelayed());
        addProjectEventHandler(WatchAddedEvent.TYPE, event -> refreshDelayed());
        asWidget().addAttachHandler(event -> onRefresh());
        addPortletAction(new PortletAction("Refresh", (action, event) -> onRefresh()));
    }

    private void refreshDelayed() {
        refreshTimer.cancel();
        refreshTimer.schedule(2000);
    }

    @Override
    public void handleLogin(UserId userId) {
        onRefresh();
    }

    @Override
    public void handleLogout(UserId userId) {
        onRefresh();
    }

    private void onRefresh() {
        if (asWidget().isAttached()) {
            setTitle("Watched Entity Changes");
            permissionChecker.hasPermission(WATCH_CHANGES,
                                            canWatch -> {
                                               if(canWatch) {
                                                   presenter.setChangesForWatches(getProjectId(), loggedInUserProvider.getCurrentUserId());
                                                    setWidget(presenter.getView());
                                               }
                                               else {
                                                   setWidget(forbiddenView);
                                               }
                                            });
        }
    }
}
