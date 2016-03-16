package edu.stanford.bmir.protege.web.client.login;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.signup.WebProtegeSignupDialogController;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignUpForAccountHandlerImpl implements SignUpForAccountHandler {

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public SignUpForAccountHandlerImpl(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void handleSignUpForAccount() {
        WebProtegeDialog.showDialog(new WebProtegeSignupDialogController(dispatchServiceManager));
    }
}
