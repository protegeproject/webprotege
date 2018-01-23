package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public interface LoggedInUserView extends IsWidget {

    void setLoggedInUserName(String displayName);

    void clearLoggedInUserName();

    void setSignOutRequestHandler(SignOutRequestHandler handler);

    void setChangeEmailAddressHandler(ChangeEmailAddressHandler handler);

    void setChangePasswordHandler(ChangePasswordHandler handler);
}
