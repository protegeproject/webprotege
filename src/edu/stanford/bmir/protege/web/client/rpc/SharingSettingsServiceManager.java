package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public class SharingSettingsServiceManager {

    private static SharingSettingsServiceAsync instance = GWT.create(SharingSettingsService.class);
    
    public static SharingSettingsServiceAsync getService() {
        return instance;
    }
    
}
