package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
public class MessageBoxErrorDisplay implements DispatchErrorMessageDisplay {

    private static final Messages MESSAGES = GWT.create(Messages.class);

    private static boolean displayingError = false;

    private void displayMessageBox(String mainMessage, String subMessage) {
        GWT.log("[MessageBoxErrorDisplay] Display error. Disp: " + displayingError + " Msg: " + mainMessage);
        if (displayingError) {
            GWT.log("[MessageBoxErrorDisplay] " +
                            "Not displaying error message because an error message is already being displayed.  " +
                            "MainMessage: " + mainMessage);
            return;
        }
        displayingError = true;
        MessageBox.showAlert(mainMessage, subMessage, () -> displayingError = false);
    }

    @Override
    public void displayPermissionDeniedErrorMessage() {
        displayPermissionDeniedErrorMessage(MESSAGES.error_permissionError_msg());
    }

    @Override
    public void displayPermissionDeniedErrorMessage(String specificMessage) {
        displayMessageBox(MESSAGES.error_permissionError_title() , specificMessage);
    }

    @Override
    public void displayGeneralErrorMessage(String title, String message) {
        displayMessageBox(title, message);
    }

    @Override
    public void displayIncompatibleRemoteServiceExceptionErrorMessage() {
        MessageBox.showMessage(
                MESSAGES.error_refreshBrowser() ,
                MESSAGES.error_upgraded() );
    }

    @Override
    public void displayInvocationExceptionErrorMessage(InvocationException exception) {
        GWT.log("InvocationException: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);
        if (exception instanceof StatusCodeException) {
            StatusCodeException statusCodeException = (StatusCodeException) exception;
            if (statusCodeException.getStatusCode() != 0) {
                displayMessageBox(statusCodeException.getStatusText(),
                                  MESSAGES.error_webProtegeHasEncounteredAnErrorPleaseTryAgain() + "<br>" +
                                  MESSAGES.error_statusCode() + ": " + statusCodeException.getStatusCode() + " (" + statusCodeException
                                          .getStatusText() + ")"
                );
            }
            else {
                displayMessageBox(MESSAGES.error_connectionError_title() ,
                                  MESSAGES.error_connectionError_msg());
            }
        }
        else {
            displayMessageBox(
                    MESSAGES.error() ,
                    MESSAGES.error_general() );
        }

    }
}
