package edu.stanford.bmir.protege.web.client.dispatch;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
public interface DispatchErrorMessageDisplay {

    /**
     * Display an error message indicating that the submitted action could not be executed because the user does not
     * have permission to execute it.
     */
    void displayPermissionDeniedErrorMessage();

    /**
     * Display an error message for an {@link com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException}
     */
    void displayIncompatibleRemoteServiceExceptionErrorMessage();

    /**
     * Display an error message for an {@link com.google.gwt.user.client.rpc.InvocationException}
     */
    void displayInvocationExceptionErrorMessage();

    /**
     * Display a general error message.
     * @param title The error message title.  Not {@code null}.
     * @param message The error message.  Not {@code null}.
     */
    void displayGeneralErrorMessage(String title, String message);

}
