package edu.stanford.bmir.protege.web.client.ui.verification;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/07/2013
 * <p>
 *     A verification service provider which always verifies the user as a human user.
 * </p>
 */
public class NullHumanVerificationServiceProvider implements HumanVerificationServiceProvider {

    @Override
    public void runVerification(HumanVerificationHandler verificationHandler) {
        verificationHandler.handleVerificationSuccess();
    }
}
