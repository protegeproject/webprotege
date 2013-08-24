package edu.stanford.bmir.protege.web.client.actionbar.application;

import edu.stanford.bmir.protege.web.client.ui.signup.WebProtegeSignupDialog;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignUpForAccountHandlerImpl implements SignUpForAccountHandler {

    @Override
    public void handleSignUpForAccount() {
        WebProtegeSignupDialog dlg = new WebProtegeSignupDialog();
        dlg.setVisible(true);
    }
}
