package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.help.ShowAboutBoxHandler;
import edu.stanford.bmir.protege.web.client.help.ShowUserGuideHandler;
import edu.stanford.bmir.protege.web.client.user.ChangeEmailAddressHandler;
import edu.stanford.bmir.protege.web.client.user.ChangePasswordHandler;
import edu.stanford.bmir.protege.web.client.user.SignOutRequestHandler;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public interface ApplicationActionBar extends IsWidget {

    void setSignedInUser(UserId userId);

    void setSignInRequestHandler(SignInRequestHandler signInRequestHandler);

    void setSignOutRequestHandler(SignOutRequestHandler signOutRequestHandler);

    void setSignUpForAccountHandler(SignUpForAccountHandler signUpForAccountHandler);

    void setSignUpForAccountVisible(boolean visible);

    void setChangePasswordHandler(ChangePasswordHandler changePasswordHandler);

    void setChangeEmailAddressHandler(ChangeEmailAddressHandler changeEmailAddressHandler);

    void setShowAboutBoxHandler(ShowAboutBoxHandler showAboutBoxHandler);

    void setShowUserGuideHandler(ShowUserGuideHandler showUserGuideHandler);
}
