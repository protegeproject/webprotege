package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import static edu.stanford.protege.widgetmap.client.view.ViewHolder.MESSAGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
public class MessageBoxErrorDisplay implements DispatchErrorMessageDisplay {

    private static boolean displayingError = false;

    private static Messages messages;

    private static Messages getMessages() {
        if(messages == null) {
            messages = GWT.create(Messages.class);
        }
        return messages;
    }

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
        displayPermissionDeniedErrorMessage(getMessages().error_permissionError_msg());
    }

    @Override
    public void displayPermissionDeniedErrorMessage(String specificMessage) {
        displayMessageBox(getMessages().error_permissionError_title() , specificMessage);
    }

    @Override
    public void displayGeneralErrorMessage(String title, String message) {
        displayMessageBox(title, message);
    }

    @Override
    public void displayIncompatibleRemoteServiceExceptionErrorMessage() {
        MessageBox.showMessage(
                getMessages().error_refreshBrowser() ,
                getMessages().error_upgraded() );
    }

    @Override
    public void displayInvocationExceptionErrorMessage(InvocationException exception) {
        GWT.log("InvocationException: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);
        if (exception instanceof StatusCodeException) {
            StatusCodeException statusCodeException = (StatusCodeException) exception;
            if (statusCodeException.getStatusCode() != 0) {
                displayMessageBox(statusCodeException.getStatusText(),
                                  getMessages().error_webProtegeHasEncounteredAnErrorPleaseTryAgain() + "<br>" +
                                  getMessages().error_statusCode() + ": " + statusCodeException.getStatusCode() + " (" + statusCodeException
                                          .getStatusText() + ")"
                );
            }
            else {
                displayMessageBox(getMessages().error_connectionError_title() ,
                                  getMessages().error_connectionError_msg());
            }
        }
        else {
            displayMessageBox(
                    getMessages().error() ,
                    getMessages().error_general() );
        }

    }
}
