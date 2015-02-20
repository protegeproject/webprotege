package edu.stanford.bmir.protege.web.client.dispatch;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
public interface DispatchErrorMessageDisplay {

    void displayPermissionDeniedErrorMessage();

    void displayGeneralErrorMessage(String message);

    void displayIncompatibleRemoteServiceExceptionErrorMessage();

    void displayInvocationExceptionErrorMessage();
}
