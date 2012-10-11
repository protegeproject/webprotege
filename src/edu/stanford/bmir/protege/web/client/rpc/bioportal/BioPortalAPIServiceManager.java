package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import com.google.gwt.core.client.GWT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/10/2012
 */
public class BioPortalAPIServiceManager {

    private final static BioPortalAPIServiceAsync delegate = GWT.create(BioPortalAPIService.class);
    
    
    
    private static BioPortalAPIServiceManager instance = new BioPortalAPIServiceManager();

    private BioPortalAPIServiceManager() {
    }
    
    public static BioPortalAPIServiceAsync getServiceAsync() {
        return delegate;
    }
}
