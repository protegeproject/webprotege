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

    private final DispatchServiceManager dispatchService;

    public ForgotPasswordHandlerImpl(DispatchServiceManager dispatchService) {
        this.dispatchService = dispatchService;
    }

    @Override
    public void handleForgotPassword() {
        ResetPasswordPresenter presenter = new ResetPasswordPresenter(
                dispatchService,
                new ResetPasswordViewImpl());
        presenter.resetPassword();
    }
}
