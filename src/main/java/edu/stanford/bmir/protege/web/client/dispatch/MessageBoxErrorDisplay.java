package edu.stanford.bmir.protege.web.client.dispatch;

import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
public class MessageBoxErrorDisplay implements DispatchErrorMessageDisplay {

    @Override
    public void displayPermissionDeniedErrorMessage() {
        MessageBox.showAlert(
                "Permission denied",
                "You do not have permission to carry out the specified action.");
    }

    @Override
    public void displayGeneralErrorMessage(String title, String message) {
        MessageBox.showAlert(title, message);
    }

    @Override
    public void displayIncompatibleRemoteServiceExceptionErrorMessage() {
        MessageBox.showMessage(
                "Please refresh your browser",
                "WebProtégé has been upgraded.  Please refresh your browser.");
    }

    @Override
    public void displayInvocationExceptionErrorMessage() {
        MessageBox.showMessage(
                "Internal Error",
                "An internal error has occurred.  Please refresh your browser and try again.");
    }
}
