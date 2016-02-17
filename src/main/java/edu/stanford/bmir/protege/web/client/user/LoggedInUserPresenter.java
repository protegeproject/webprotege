package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class LoggedInUserPresenter {

    private final LoggedInUserView view;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public LoggedInUserPresenter(LoggedInUserView view,
                                 LoggedInUserProvider loggedInUserManager,
                                 SignOutRequestHandler signOutRequestHandler,
                                 ChangeEmailAddressHandler changeEmailAddressHandler,
                                 ChangePasswordHandler changePasswordHandler) {
        this.view = view;
        this.loggedInUserProvider = loggedInUserManager;
        view.setSignOutRequestHandler(signOutRequestHandler);
        view.setChangeEmailAddressHandler(changeEmailAddressHandler);
        view.setChangePasswordHandler(changePasswordHandler);
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        UserId user = loggedInUserProvider.getCurrentUserId();
        view.setLoggedInUserName(user.getUserName());
    }
}
