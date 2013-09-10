package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.user.client.ui.IsWidget;
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

    void setChangePasswordHandler(ChangePasswordHandler changePasswordHandler);

    void setShowAboutBoxHandler(ShowAboutBoxHandler showAboutBoxHandler);

    void setShowUserGuideHandler(ShowUserGuideHandler showUserGuideHandler);
}
