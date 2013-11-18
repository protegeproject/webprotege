package edu.stanford.bmir.protege.web.client.ui.verification;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/07/2013
 * <p>
 *     An interface to an object which provides a service to verify that input is provided by a human user (e.g.
 *     ReCaptcha).
 * </p>
 */
public interface HasHumanVerificationServiceProvider {

    /**
     * Gets the verification service provider.
     * @return The VerificationServiceProvider. Not {@code null}.
     */
    HumanVerificationServiceProvider getVerificationServiceProvider();
}
