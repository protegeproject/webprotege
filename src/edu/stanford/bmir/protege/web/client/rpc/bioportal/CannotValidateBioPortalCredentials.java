package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class CannotValidateBioPortalCredentials extends RuntimeException implements Serializable {

    public CannotValidateBioPortalCredentials() {
        super("Either the username or password is incorrect");
    }


}
