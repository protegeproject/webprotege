package edu.stanford.bmir.protege.web.client.banner;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.actionbar.application.*;
import edu.stanford.bmir.protege.web.client.actionbar.project.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;
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

    public BannerPresenter(EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        Boolean accountCreationEnabled = Application.get().getClientApplicationProperty(
                WebProtegePropertyName.USER_ACCOUNT_CREATION_ENABLED, true);
        GWT.log("Account creation enabled: " + accountCreationEnabled);


        final ProjectActionBar projectActionBar = bannerView.getProjectActionBar();
        projectActionBar.setProjectId(Application.get().getActiveProject());
        projectActionBar.setShowShareSettingsHandler(new ShareSettingsHandlerImpl(eventBus, dispatchServiceManager));
        projectActionBar.setShowFreshEntitySettingsHandler(new ShowFreshEntitySettingsHandlerImpl(dispatchServiceManager));
        projectActionBar.setShowProjectDetailsHandler(new ShowProjectDetailsHandlerImpl(eventBus, dispatchServiceManager));
        projectActionBar.setUploadAndMergeHandler(new UploadAndMergeHandlerImpl(dispatchServiceManager));
        final ApplicationActionBar w = bannerView.getApplicationActionBar();
        w.setSignInRequestHandler(new SignInRequestHandlerImpl(dispatchServiceManager));
        w.setSignOutRequestHandler(new SignOutRequestHandlerImpl());
        w.setSignUpForAccountHandler(new SignUpForAccountHandlerImpl(dispatchServiceManager));
        w.setChangePasswordHandler(new ChangePasswordHandlerImpl(dispatchServiceManager));
        w.setChangeEmailAddressHandler(new ChangeEmailAddressHandlerImpl(dispatchServiceManager));
        w.setShowAboutBoxHandler(new ShowAboutBoxHandlerImpl());
        w.setShowUserGuideHandler(new ShowUserGuideHandlerImpl());

        updateSignedInUser(Application.get().getUserId());

        eventBus.addHandler(ActiveProjectChangedEvent.TYPE, new ActiveProjectChangedHandler() {
            @Override
            public void handleActiveProjectChanged(ActiveProjectChangedEvent event) {
                projectActionBar.setProjectId(event.getProjectId());
            }
        });
        eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                updateSignedInUser(event.getUserId());
                projectActionBar.setProjectId(Application.get().getActiveProject());
            }
        });
        eventBus.addHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                updateSignedInUser(UserId.getGuest());
                projectActionBar.setProjectId(Application.get().getActiveProject());
            }
        });
    }

    public BannerView getView() {
        return bannerView;
    }

    private void updateSignedInUser(UserId userId) {
        final ApplicationActionBar bar = bannerView.getApplicationActionBar();
        bar.setSignedInUser(userId);
        Boolean accountCreationEnabled = Application.get().getClientApplicationProperty(
                WebProtegePropertyName.USER_ACCOUNT_CREATION_ENABLED, true);
        if(accountCreationEnabled) {
            bar.setSignUpForAccountVisible(userId.isGuest());
        }
        else {
            bar.setSignUpForAccountVisible(false);
        }
    }
}
