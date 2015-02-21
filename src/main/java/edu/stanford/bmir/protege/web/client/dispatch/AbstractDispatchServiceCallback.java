package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.web.bindery.event.shared.UmbrellaException;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
public abstract class AbstractDispatchServiceCallback<T> implements AsyncCallback<T>  {

    private DispatchErrorMessageDisplay errorMessageDisplay;

    public AbstractDispatchServiceCallback() {
        errorMessageDisplay = new MessageBoxErrorDisplay();
    }

    public AbstractDispatchServiceCallback(DispatchErrorMessageDisplay errorMessageDisplay) {
        this.errorMessageDisplay = errorMessageDisplay;
    }

    /**
     * Called when the action is submitted for execution.
     * This can be used to display a progress indicator to the user, for example.
     */
    public void handleSubmittedForExecution() {

    }

    @Override
    public final void onFailure(Throwable throwable) {
        try {
            if(throwable instanceof ActionExecutionException) {
                handleExecutionException(throwable.getCause());
            }
            else if(throwable instanceof PermissionDeniedException) {
                handlePermissionDeniedException((PermissionDeniedException) throwable);
            }
            else if(throwable instanceof IncompatibleRemoteServiceException) {
                _handleIncompatibleRemoteServiceException((IncompatibleRemoteServiceException) throwable);
            }
            else if(throwable instanceof InvocationException) {
                _handleInvocationException((InvocationException) throwable);
            }
            else if(throwable instanceof UmbrellaException) {
                _handleUmbrellaException((UmbrellaException) throwable);
            }
            else {
                // Should we actually get here?  I don't think so.
                displayAndLogError(throwable);
            }
        } finally {
            try {
                handleErrorFinally(throwable);
            } finally {
                handleFinally();
            }
        }
    }

    @Override
    public final void onSuccess(T t) {
        try {
            handleSuccess(t);
        } finally {
            handleFinally();
        }
    }

    /**
     * Handles success.
     * @param t The return value from the call.
     */
    public void handleSuccess(T t) {

    }


    /**
     * Called after some kind of error has occurred.  This can be used to clean up after all errors.
     * @param throwable The error that occurred.
     */
    public void handleErrorFinally(Throwable throwable) {

    }

    /**
     * Called after either {@link #handleSuccess(Object)} or {@link #handleErrorFinally(Throwable)}.  This can be used
     * to perform clean up that should take place whether the call succeeded or failed.  It is like the "finally"
     * block in a "try-catch-finally" statement.
     */
    public void handleFinally() {

    }

    public void handleExecutionException(Throwable cause) {
        displayAndLogError(cause);
    }

    public void handlePermissionDeniedException(PermissionDeniedException e) {
        errorMessageDisplay.displayPermissionDeniedErrorMessage();
    }

    private void displayAndLogError(Throwable throwable) {
        Logger logger = Logger.getLogger("AsyncCallbackErrorLogger");
        logger.log(Level.SEVERE, "Error executing remote call: " + throwable.getMessage());
        errorMessageDisplay.displayGeneralErrorMessage(getErrorMessage(throwable));
    }

    private void _handleIncompatibleRemoteServiceException(IncompatibleRemoteServiceException e) {
        errorMessageDisplay.displayIncompatibleRemoteServiceExceptionErrorMessage();
    }

    private void _handleInvocationException(InvocationException e) {
        errorMessageDisplay.displayInvocationExceptionErrorMessage();
    }

    private void _handleUmbrellaException(UmbrellaException e) {
        for(Throwable cause : e.getCauses()) {
            onFailure(cause);
        }
    }

    protected String getErrorMessageTitle() {
        return "Error";
    }

    protected String getErrorMessage(Throwable throwable) {
        return "An error has occurred.  Please try again.";
    }



}
