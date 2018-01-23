package edu.stanford.bmir.protege.web.client.verification;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/07/2013
 */
public interface HumanVerificationHandler {

    void handleVerificationSuccess();

    void handleVerificationFailure(String errorMessage);
}
