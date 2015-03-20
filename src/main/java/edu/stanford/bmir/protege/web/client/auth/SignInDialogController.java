package edu.stanford.bmir.protege.web.client.auth;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.auth.SignInDetails;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class SignInDialogController extends WebProtegeOKCancelDialogController<SignInDetails> {

    private SignInView signInView;

    public SignInDialogController(final SignInView signInView) {
        super("Sign in");
        this.signInView = signInView;
        this.signInView.setForgotPasswordHandler(new ForgotPasswordHandlerImpl());
        addDialogValidator(new WebProtegeDialogValidator() {
            @Override
            public ValidationState getValidationState() {
                return signInView.getUserName().trim().isEmpty() ? ValidationState.INVALID : ValidationState.VALID;
            }

            @Override
            public String getValidationMessage() {
                return "Please enter your user name";
            }
        });

        addDialogValidator(new WebProtegeDialogValidator() {
            @Override
            public ValidationState getValidationState() {
                return signInView.getPassword().isEmpty() ? ValidationState.INVALID : ValidationState.VALID;
            }

            @Override
            public String getValidationMessage() {
                return "Please enter your password";
            }
        });
    }

    @Override
    public Widget getWidget() {
        return signInView.asWidget();
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return signInView.getInitialFocusable();
    }

    @Override
    public SignInDetails getData() {
        String userName = signInView.getUserName();
        String password = signInView.getPassword();
        return new SignInDetails(userName, password);
    }
}
