package edu.stanford.bmir.protege.web.client.dispatch;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class UIDescription {

    private String message;

    public UIDescription(String permissionDeniedMessage) {
        this.message = permissionDeniedMessage;
    }

    public String getPermissionDeniedMessage() {
        return message;
    }
}
