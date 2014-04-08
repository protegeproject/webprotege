package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/10/2012
 */
public class BioPortalAPIServiceException extends RuntimeException implements Serializable {

    public BioPortalAPIServiceException() {
    }

    public BioPortalAPIServiceException(String message) {
        super(message);
    }

    public BioPortalAPIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
