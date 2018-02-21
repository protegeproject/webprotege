package edu.stanford.bmir.protege.web.client.logout;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/02/16
 */
public class LogoutPresenter {

    private final LogoutView logoutView;

    private final LoggedInUserManager loggedInUserManager;

    private final PlaceController placeController;

    @Inject
    public LogoutPresenter(LogoutView logoutView, LoggedInUserManager loggedInUserManager, PlaceController placeController) {
        this.logoutView = logoutView;
        this.loggedInUserManager = loggedInUserManager;
        this.placeController = placeController;
        logoutView.setLogoutHandler(new LogoutHandler() {
            @Override
            public void handleLogout() {
                doLogout();
            }
        });
    }

    public IsWidget getView() {
        return logoutView;
    }

    private void doLogout() {
        loggedInUserManager.logOutCurrentUser();
    }
}
