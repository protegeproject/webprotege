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


    public static SignInPresenter get() {
        return new SignInPresenter(
                getAuthenticatedActionExecutor(), new SignInDialogPresenter(new SignInDialogController(new SignInViewImpl())),
                new MessageBoxSignInMessageDisplay(),
                new SignInSuccessfulHandler() {
                    @Override
                    public void handleLoginSuccessful(UserId userId) {
                        Application.get().setCurrentUser(userId);
                    }
                }
        );
    }

    public SignInPresenter(AuthenticatedActionExecutor loginExecutor,
                           SignInDialogPresenter signInDialogPresenter,
                           SignInMessageDisplay signInMessageDisplay,
                           SignInSuccessfulHandler signInSuccessfulHandler) {
        this.dialogPresenter = signInDialogPresenter;
        this.loginExecutor = loginExecutor;
        this.signInMessageDisplay = signInMessageDisplay;
        this.signInSuccessfulHandler = signInSuccessfulHandler;
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


    private static AuthenticatedActionExecutor getAuthenticatedActionExecutor() {
        Provider<MessageDigestAlgorithm> digestAlgorithmProvider = new Md5DigestAlgorithmProvider();
        PasswordDigestAlgorithm passwordDigestAlgorithm = new PasswordDigestAlgorithm(digestAlgorithmProvider);
        ChapResponseDigestAlgorithm chapResponseDigestAlgorithm = new ChapResponseDigestAlgorithm(digestAlgorithmProvider);
        return new AuthenticatedActionExecutor(DispatchServiceManager.get(), passwordDigestAlgorithm, chapResponseDigestAlgorithm);
    }
}
