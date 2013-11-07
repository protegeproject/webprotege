package edu.stanford.bmir.protege.web.client.actionbar.application;

import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.signup.WebProtegeSignupDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignUpForAccountHandlerImpl implements SignUpForAccountHandler {

    @Override
    public void handleSignUpForAccount() {
        WebProtegeDialog.showDialog(new WebProtegeSignupDialogController());
    }
}
