package edu.stanford.bmir.protege.web.client.auth;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class SignInPresenter {

    private final SignInDialogPresenter dialogPresenter;

    private final AuthenticatedActionExecutor loginExecutor;

    private final SignInMessageDisplay signInMessageDisplay;

    private final SignInSuccessfulHandler signInSuccessfulHandler;

    private final DispatchServiceManager dispatchServiceManager;

    public static SignInPresenter get(DispatchServiceManager dispatchServiceManager) {
        return new SignInPresenter(
                getAuthenticatedActionExecutor(dispatchServiceManager), new SignInDialogPresenter(new SignInDialogController(new SignInViewImpl(), dispatchServiceManager)),
                new MessageBoxSignInMessageDisplay(),
                new SignInSuccessfulHandler() {
                    @Override
                    public void handleLoginSuccessful(UserId userId) {
                        Application.get().setCurrentUser(userId);
                    }
                }, dispatchServiceManager
        );
    }

    public SignInPresenter(AuthenticatedActionExecutor loginExecutor,
                           SignInDialogPresenter signInDialogPresenter,
                           SignInMessageDisplay signInMessageDisplay,
                           SignInSuccessfulHandler signInSuccessfulHandler,
                           DispatchServiceManager dispatchServiceManager) {
        this.dialogPresenter = signInDialogPresenter;
        this.loginExecutor = loginExecutor;
        this.signInMessageDisplay = signInMessageDisplay;
        this.signInSuccessfulHandler = signInSuccessfulHandler;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void showLoginDialog() {
        dialogPresenter.showDialog(new WebProtegeDialogButtonHandler<SignInDetails>() {
            @Override
            public void handleHide(SignInDetails data, WebProtegeDialogCloser closer) {
                handleSignIn(data, closer);
            }
        });
    }

    private void handleSignIn(SignInDetails signInDetails, final WebProtegeDialogCloser closer) {
        final UserId userId = UserId.getUserId(signInDetails.getUserName());
        loginExecutor.execute(userId, signInDetails.getClearTextPassword(),
                new PerformLoginActionFactory(),
                new DispatchServiceCallback<AuthenticationResponse>() {
                    @Override
                    public void handleSuccess(AuthenticationResponse response) {
                        handleAuthenticationResponse(userId, response, closer);
                    }
                });
    }

    private void handleAuthenticationResponse(UserId userId, AuthenticationResponse response, WebProtegeDialogCloser closer) {
        if(response == AuthenticationResponse.SUCCESS) {
            signInSuccessfulHandler.handleLoginSuccessful(userId);
            closer.hide();
        }
        else {
            signInMessageDisplay.displayLoginFailedMessage();
        }
    }


    private static AuthenticatedActionExecutor getAuthenticatedActionExecutor(DispatchServiceManager dispatchServiceManager) {
        Provider<MessageDigestAlgorithm> digestAlgorithmProvider = new Md5DigestAlgorithmProvider();
        PasswordDigestAlgorithm passwordDigestAlgorithm = new PasswordDigestAlgorithm(digestAlgorithmProvider);
        ChapResponseDigestAlgorithm chapResponseDigestAlgorithm = new ChapResponseDigestAlgorithm(digestAlgorithmProvider);
        return new AuthenticatedActionExecutor(dispatchServiceManager, passwordDigestAlgorithm, chapResponseDigestAlgorithm);
    }
}
