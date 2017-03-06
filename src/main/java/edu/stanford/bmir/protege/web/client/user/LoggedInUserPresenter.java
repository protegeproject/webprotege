package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class LoggedInUserPresenter implements HasDispose, Presenter {

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

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        eventBus.addHandler(UserLoggedInEvent.ON_USER_LOGGED_IN, event -> updateButton());
        eventBus.addHandler(UserLoggedOutEvent.ON_USER_LOGGED_OUT, event -> updateButton());
        updateButton();
        container.setWidget(view);
    }

    private void updateButton() {
        UserId user = loggedInUserProvider.getCurrentUserId();
        view.setLoggedInUserName(user.getUserName());
    }

    @Override
    public void dispose() {
    }
}
