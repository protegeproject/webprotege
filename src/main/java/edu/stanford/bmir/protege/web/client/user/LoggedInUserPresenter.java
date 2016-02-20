package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class LoggedInUserPresenter implements HasDispose {

    private final LoggedInUserView view;

    private final LoggedInUserProvider loggedInUserProvider;

    private final HandlerRegistration inReg;

    private final HandlerRegistration outReg;

    @Inject
    public LoggedInUserPresenter(LoggedInUserView view,
                                 LoggedInUserProvider loggedInUserManager,
                                 SignOutRequestHandler signOutRequestHandler,
                                 ChangeEmailAddressHandler changeEmailAddressHandler,
                                 ChangePasswordHandler changePasswordHandler,
                                 EventBus eventBus) {
        this.view = view;
        this.loggedInUserProvider = loggedInUserManager;
        view.setSignOutRequestHandler(signOutRequestHandler);
        view.setChangeEmailAddressHandler(changeEmailAddressHandler);
        view.setChangePasswordHandler(changePasswordHandler);
        inReg = eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                updateButton();
            }
        });
        outReg = eventBus.addHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                updateButton();
            }
        });
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        updateButton();
    }

    private void updateButton() {
        UserId user = loggedInUserProvider.getCurrentUserId();
        view.setLoggedInUserName(user.getUserName());
    }

    @Override
    public void dispose() {
        inReg.removeHandler();
        outReg.removeHandler();
    }
}
