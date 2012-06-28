package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 */
public class UserProfileManagerServiceManager {

    private static UserProfileManagerServiceAsync instance = GWT.create(UserProfileManagerService.class);
    
    public static UserProfileManagerServiceAsync getService() {
        return instance;
    }
}
