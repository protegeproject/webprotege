package edu.stanford.bmir.protege.web.server;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class UIConfigurationManagerException extends RuntimeException implements Serializable {

    private UIConfigurationManagerException() {
    }

    public UIConfigurationManagerException(String message) {
        super(message);
    }
}
