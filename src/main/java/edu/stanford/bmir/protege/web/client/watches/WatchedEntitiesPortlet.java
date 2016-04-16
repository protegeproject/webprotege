package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletActionHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedHandler;

import javax.inject.Inject;

public class WatchedEntitiesPortlet extends AbstractWebProtegePortlet {

    private final LoggedInUserProvider loggedInUserProvider;

    private final ChangeListViewPresenter presenter;

    private final Timer refreshTimer = new Timer() {
        @Override
        public void run() {
            onRefresh();
        }
    };

    @Inject
    public WatchedEntitiesPortlet(ChangeListViewPresenter presenter, SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        this.loggedInUserProvider = loggedInUserProvider;
        this.presenter = presenter;
        ChangeListView changeListView = presenter.getView();
        ScrollPanel scrollPanel = new ScrollPanel(changeListView.asWidget());
        getContentHolder().setWidget(scrollPanel);
        onRefresh();
        addProjectEventHandler(WatchAddedEvent.TYPE, new WatchAddedHandler() {
            @Override
            public void handleWatchAdded(WatchAddedEvent event) {
                refreshDelayed();
            }
        });
        addProjectEventHandler(WatchAddedEvent.TYPE, new WatchAddedHandler() {
            @Override
            public void handleWatchAdded(WatchAddedEvent event) {
                refreshDelayed();
            }
        });
        asWidget().addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                onRefresh();
            }
        });
        addPortletAction(new PortletAction("Refresh", new PortletActionHandler() {
            @Override
            public void handleActionInvoked(PortletAction action, ClickEvent event) {
                onRefresh();
            }
        }));
    }

    private void refreshDelayed() {
        refreshTimer.cancel();
        refreshTimer.schedule(2000);
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
        if (asWidget().isAttached()) {
            presenter.setChangesForWatches(getProjectId(), getUserId());
            setTitle("Watched Entity Changes");
        }
    }
}
