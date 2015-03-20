package edu.stanford.bmir.protege.web.client.auth;

import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.shared.auth.SignInDetails;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class SignInDialogPresenter {

    private final SignInDialogController signInDialogController;

    public SignInDialogPresenter(SignInDialogController signInDialogController) {
        this.signInDialogController = signInDialogController;
    }

    public void showDialog(WebProtegeDialogButtonHandler<SignInDetails> signInButtonHandler) {
        signInDialogController.setDialogButtonHandler(DialogButton.OK, signInButtonHandler);
        WebProtegeDialog.showDialog(signInDialogController);
    }
}
