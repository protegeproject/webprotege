package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.user.client.Timer;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewPresenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.WATCH_CHANGES;

@Portlet(id = "portlets.WatchedEntities", title = "Watched Entities")
public class WatchedEntitiesPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final LoggedInUserProvider loggedInUserProvider;

    private final ChangeListViewPresenter presenter;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final Timer refreshTimer = new Timer() {
        @Override
        public void run() {
            onRefresh();
        }
    };

    private final ChangeListView changeListView;

    private Optional<PortletUi> ui = Optional.empty();

    @Inject
    public WatchedEntitiesPortletPresenter(ChangeListViewPresenter presenter,
                                           LoggedInUserProjectPermissionChecker permissionChecker,
                                           SelectionModel selectionModel,
                                           ProjectId projectId,
                                           LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, projectId);
        this.loggedInUserProvider = loggedInUserProvider;
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
        changeListView = presenter.getView();

    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        ui = Optional.of(portletUi);
        portletUi.setWidget(changeListView);
        portletUi.addPortletAction(new PortletAction("Refresh", (action, event) -> onRefresh()));

        eventBus.addProjectEventHandler(getProjectId(),
                                        WatchAddedEvent.TYPE, event -> refreshDelayed());
        eventBus.addProjectEventHandler(getProjectId(),
                                        WatchAddedEvent.TYPE, event -> refreshDelayed());
        eventBus.addProjectEventHandler(getProjectId(),
                                        PermissionsChangedEvent.ON_PERMISSIONS_CHANGED,
                                        event -> onRefresh());
        portletUi.setForbiddenMessage("You do not have permission to watch changes in this project");
        portletUi.setViewTitle("Watched Entity Changes");

        // TODO: Check this

        onRefresh();
    }

    private void refreshDelayed() {
        refreshTimer.cancel();
        refreshTimer.schedule(2000);
    }

    private void onRefresh() {

        ui.ifPresent(portletUi -> {
            permissionChecker.hasPermission(WATCH_CHANGES,
                                            canWatch -> {
                                                if (canWatch) {
                                                    presenter.setChangesForWatches(getProjectId(),
                                                                                   loggedInUserProvider.getCurrentUserId());
                                                    portletUi.setForbiddenVisible(false);
                                                }
                                                else {
                                                    portletUi.setForbiddenVisible(false);
                                                }
                                            });
        });

    }
}
