package edu.stanford.bmir.protege.web.client.auth;

import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class MessageBoxSignInMessageDisplay implements SignInMessageDisplay {

    @Override
    public void displayLoginFailedMessage() {
        MessageBox.showMessage("Sign in failed", "Incorrect user name or password");
    }
}
