package edu.stanford.bmir.protege.web.client.banner;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.actionbar.application.*;
import edu.stanford.bmir.protege.web.client.actionbar.project.ProjectActionBar;
import edu.stanford.bmir.protege.web.client.actionbar.project.ShareSettingsHandlerImpl;
import edu.stanford.bmir.protege.web.client.actionbar.project.ShowFreshEntitySettingsHandlerImpl;
import edu.stanford.bmir.protege.web.client.actionbar.project.ShowProjectDetailsHandlerImpl;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class BannerPresenter {

    private BannerView bannerView = new BannerViewImpl();

    public BannerPresenter() {
        final ProjectActionBar projectActionBar = bannerView.getProjectActionBar();
        projectActionBar.setProjectId(Application.get().getActiveProject());
        projectActionBar.setShowShareSettingsHandler(new ShareSettingsHandlerImpl());
        projectActionBar.setShowFreshEntitySettingsHandler(new ShowFreshEntitySettingsHandlerImpl());
        projectActionBar.setShowProjectDetailsHandler(new ShowProjectDetailsHandlerImpl());
        EventBusManager.getManager().registerHandler(ActiveProjectChangedEvent.TYPE, new ActiveProjectChangedHandler() {
            @Override
            public void handleActiveProjectChanged(ActiveProjectChangedEvent event) {
                projectActionBar.setProjectId(event.getProjectId());
            }
        });
        final ApplicationActionBar w = bannerView.getApplicationActionBar();
        w.setSignedInUser(Application.get().getUserId());
        w.setSignInRequestHandler(new SignInRequestHandlerImpl());
        w.setSignOutRequestHandler(new SignOutRequestHandlerImpl());
        w.setSignUpForAccountHandler(new SignUpForAccountHandlerImpl());
        w.setChangePasswordHandler(new ChangePasswordHandlerImpl());
        w.setShowAboutBoxHandler(new ShowAboutBoxHandlerImpl());
        w.setShowUserGuideHandler(new ShowUserGuideHandlerImpl());
        EventBusManager.getManager().registerHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                w.setSignedInUser(event.getUserId());
                projectActionBar.setProjectId(Application.get().getActiveProject());
            }
        });
        EventBusManager.getManager().registerHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                w.setSignedInUser(UserId.getGuest());
                projectActionBar.setProjectId(Application.get().getActiveProject());
            }
        });
    }

    public BannerView getView() {
        return bannerView;
    }
}
