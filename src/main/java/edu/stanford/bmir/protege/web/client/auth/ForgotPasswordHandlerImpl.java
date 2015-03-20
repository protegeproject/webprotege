package edu.stanford.bmir.protege.web.client.auth;

import edu.stanford.bmir.protege.web.client.chgpwd.ResetPasswordPresenter;
import edu.stanford.bmir.protege.web.client.chgpwd.ResetPasswordViewImpl;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class ForgotPasswordHandlerImpl implements ForgotPasswordHandler {

    @Override
    public void handleForgotPassword() {
        ResetPasswordPresenter presenter = new ResetPasswordPresenter(
                DispatchServiceManager.get(),
                new ResetPasswordViewImpl());
        presenter.resetPassword();
    }
}
