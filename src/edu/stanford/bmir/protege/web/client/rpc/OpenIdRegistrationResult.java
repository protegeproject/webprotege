package edu.stanford.bmir.protege.web.client.rpc;


import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class OpenIdRegistrationResult implements Serializable {

    OpenIdRegistrationCode result;

    private OpenIdRegistrationResult() {
    }

    public OpenIdRegistrationResult(OpenIdRegistrationCode result) {
        this.result = result;
    }

    public OpenIdRegistrationCode getResult() {
        return result;
    }
}
