package edu.stanford.bmir.protege.web.client.login;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.actionbar.application.SignInRequestHandler;
import edu.stanford.bmir.protege.web.client.chgpwd.ResetPasswordPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class LoginPresenter {

    private final LoginView view;

    private final AuthenticatedActionExecutor loginExecutor;

    private final LoggedInUserManager loggedInUserManager;

    private final PlaceController placeController;

    private final ResetPasswordPresenter resetPasswordPresenter;

    private Optional<Place> nextPlace = Optional.absent();



    @Inject
    public LoginPresenter(LoginView view,
                          AuthenticatedActionExecutor loginExecutor,
                          LoggedInUserManager loggedInUserManager,
                          PlaceController placeController,
                          ResetPasswordPresenter resetPasswordPresenter) {
        this.view = view;
        this.loginExecutor = loginExecutor;
        this.loggedInUserManager = loggedInUserManager;
        this.placeController = placeController;
        this.resetPasswordPresenter = resetPasswordPresenter;
        view.setSignInHandler(new SignInRequestHandler() {
            @Override
            public void handleSignInRequest() {
                handleSignIn();
            }
        });
        view.setForgotPasswordHandler(new ForgotPasswordHandler() {
            @Override
            public void handleForgotPassword() {
                handleResetPassword();
            }
        });
    }

    public LoginView getView() {
        return view;
    }

    public void setNextPlace(Place nextPlace) {
        this.nextPlace = Optional.of(nextPlace);
    }

    public void start() {
        view.clearView();
        view.hideErrorMessages();
    }

    private void handleSignIn() {
        view.hideErrorMessages();
        String userName = view.getUserName();
        if(userName.isEmpty()) {
            view.showUserNameRequiredErrorMessage();
            return;
        }
        String password = view.getPassword();
        if(password.isEmpty()) {
            view.showPasswordRequiredErrorMessage();
            return;
        }
        SignInDetails signInDetails = new SignInDetails(userName, password);
        handleSignIn(signInDetails);
    }

    private void handleResetPassword() {
        resetPasswordPresenter.resetPassword();
    }

    private void handleSignIn(SignInDetails signInDetails) {
        final UserId userId = UserId.getUserId(signInDetails.getUserName());
        loginExecutor.execute(userId, signInDetails.getClearTextPassword(),
                new PerformLoginActionFactory(),
                new DispatchServiceCallback<AuthenticationResponse>() {
                    @Override
                    public void handleSuccess(AuthenticationResponse response) {
                        handleAuthenticationResponse(userId, response);
                    }
                });
    }

    private void handleAuthenticationResponse(UserId userId, AuthenticationResponse response) {
        if(response == AuthenticationResponse.SUCCESS) {
            GWT.log("[LoginPresenter] Login successful.  Logged in user: " + userId + "  Next place: " + nextPlace);
            loggedInUserManager.setLoggedInUser(userId, new AsyncCallback<UserDetails>() {
                @Override
                public void onFailure(Throwable caught) {
                    GWT.log("[LoginPresenter] Switching user failed");
                }

                @Override
                public void onSuccess(UserDetails result) {
                    GWT.log("[LoginPresenter] Switched user");
                    // Next place
                    if(nextPlace.isPresent()) {
                        placeController.goTo(nextPlace.get());
                    }
                }
            });

        }
        else {
            view.showLoginFailedErrorMessage();
        }
    }
}
