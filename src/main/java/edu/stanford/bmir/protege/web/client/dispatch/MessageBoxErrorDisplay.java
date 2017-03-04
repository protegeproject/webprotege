package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
public class MessageBoxErrorDisplay implements DispatchErrorMessageDisplay {

    @Override
    public void displayPermissionDeniedErrorMessage() {
        displayPermissionDeniedErrorMessage("You do not have permission to carry out the specified action.");
    }

    @Override
    public void displayPermissionDeniedErrorMessage(String specificMessage) {
        MessageBox.showAlert(
                "Permission denied",
                specificMessage);
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
    public void displayInvocationExceptionErrorMessage(InvocationException exception) {
        GWT.log("InvocationException: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);
        if(exception instanceof StatusCodeException) {
            StatusCodeException statusCodeException = (StatusCodeException) exception;
            if (statusCodeException.getStatusCode() != 0) {
                MessageBox.showAlert(statusCodeException.getStatusText(),
                        "WebProtégé has encountered an error. Please try again.<br>" +
                                "Status Code: " + statusCodeException.getStatusCode() + " (" + statusCodeException.getStatusText() + ")"
                );
            }
            else {
                MessageBox.showAlert("Connection Error",
                        "WebProtégé cannot connect to the server.  Please check your network connection.");
            }
        }
        else {
            MessageBox.showMessage(
                    "Error",
                    "An error has occurred.  Please refresh your browser and try again.");
        }

    }
}
