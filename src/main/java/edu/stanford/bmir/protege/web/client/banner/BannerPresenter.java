package edu.stanford.bmir.protege.web.client.banner;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.*;
import edu.stanford.bmir.protege.web.client.actionbar.application.*;
import edu.stanford.bmir.protege.web.client.actionbar.project.*;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.help.ShowAboutBoxHandler;
import edu.stanford.bmir.protege.web.client.help.ShowUserGuideHandler;
import edu.stanford.bmir.protege.web.client.project.*;
import edu.stanford.bmir.protege.web.client.user.ChangeEmailAddressHandler;
import edu.stanford.bmir.protege.web.client.user.ChangePasswordHandler;
import edu.stanford.bmir.protege.web.client.user.SignOutRequestHandler;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class BannerPresenter {

    private final BannerView bannerView;

    private final HasClientApplicationProperties hasClientApplicationProperties;

    private final ActiveProjectManager activeProjectManager;

    private final ProjectManager projectManager;

    private final LoggedInUserManager loggedInUserManager;

    private final ProjectActionBar projectActionBar;

    @Inject
    public BannerPresenter(EventBus eventBus,
                           BannerView view,
                           HasClientApplicationProperties hasClientApplicationProperties,
                           LoggedInUserManager loggedInUserManager,
                           ProjectManager projectManager,
                           ActiveProjectManager activeProjectManager,
                           ShowShareSettingsHandler showShareSettingsHandler,
                           ShowFreshEntitySettingsHandler showFreshEntitySettingsHandler,
                           ShowProjectDetailsHandler showProjectDetailsHandler,
                           UploadAndMergeHandler uploadAndMergeHandler,
                           SignInRequestHandler signInRequestHandler,
                           SignOutRequestHandler signOutRequestHandler,
                           SignUpForAccountHandler signUpForAccountHandler,
                           ChangePasswordHandler changePasswordHandler,
                           ChangeEmailAddressHandler changeEmailAddressHandler,
                           ShowAboutBoxHandler showAboutBoxHandler,
                           ShowUserGuideHandler showUserGuideHandler
                           ) {
        this.bannerView = view;
        this.activeProjectManager = activeProjectManager;
        this.projectManager = projectManager;
        this.loggedInUserManager = loggedInUserManager;
        this.hasClientApplicationProperties = hasClientApplicationProperties;
        projectActionBar = bannerView.getProjectActionBar();
        projectActionBar.setShowShareSettingsHandler(showShareSettingsHandler);
        projectActionBar.setShowFreshEntitySettingsHandler(showFreshEntitySettingsHandler);
        projectActionBar.setShowProjectDetailsHandler(showProjectDetailsHandler);
        projectActionBar.setUploadAndMergeHandler(uploadAndMergeHandler);
        final ApplicationActionBar w = bannerView.getApplicationActionBar();
        w.setSignInRequestHandler(signInRequestHandler);
        w.setSignOutRequestHandler(signOutRequestHandler);
        w.setSignUpForAccountHandler(signUpForAccountHandler);
        w.setChangePasswordHandler(changePasswordHandler);
        w.setChangeEmailAddressHandler(changeEmailAddressHandler);
        w.setShowAboutBoxHandler(showAboutBoxHandler);
        w.setShowUserGuideHandler(showUserGuideHandler);

        updateSignedInUser(loggedInUserManager.getCurrentUserId());
        updateProjectActionBar();

        eventBus.addHandler(ActiveProjectChangedEvent.TYPE, new ActiveProjectChangedHandler() {
            @Override
            public void handleActiveProjectChanged(ActiveProjectChangedEvent event) {
                updateProjectActionBar();
            }
        });
        eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                updateSignedInUser(event.getUserId());
                updateProjectActionBar();
            }
        });
        eventBus.addHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                updateSignedInUser(UserId.getGuest());
                updateProjectActionBar();
            }
        });
    }

    private void updateProjectActionBar() {
        boolean visible = isLoggedInUserOwnerOfActiveProject();
        projectActionBar.asWidget().setVisible(visible);
    }

    private boolean isLoggedInUserOwnerOfActiveProject() {
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if(!projectId.isPresent()) {
            return false;
        }
        Optional<Project> project = projectManager.getProject(projectId.get());
        if(!project.isPresent()) {
            return false;
        }
        UserId loggedInUserId = loggedInUserManager.getLoggedInUserId();
        if(loggedInUserId.isGuest()) {
            return false;
        }
        Project theProject = project.get();
        UserId projectOwnerUserId = theProject.getProjectDetails().getOwner();
        return loggedInUserId.equals(projectOwnerUserId);
    }

    public BannerView getView() {
        return bannerView;
    }

    private void updateSignedInUser(UserId userId) {
        final ApplicationActionBar bar = bannerView.getApplicationActionBar();
        bar.setSignedInUser(userId);
        Boolean accountCreationEnabled = hasClientApplicationProperties.getClientApplicationProperty(
                WebProtegePropertyName.USER_ACCOUNT_CREATION_ENABLED, true);
        if(accountCreationEnabled) {
            bar.setSignUpForAccountVisible(userId.isGuest());
        }
        else {
            bar.setSignUpForAccountVisible(false);
        }
    }
}
