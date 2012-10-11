package edu.stanford.bmir.protege.web.server.rest;

import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalRestCall<T> extends RestCall<T> {

    public BioPortalRestCall(String restCallName, Class<T> responseObjectClass) {
        super(BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL, restCallName, responseObjectClass);
    }

    public BioPortalRestCall(String restServiceBase, String restCallName, Class<T> responseObjectClass) {
        super(restServiceBase, restCallName, responseObjectClass);
    }

    private void addAPIKey() {
        addParameterValue(BioPortalConstants.API_KEY_PROPERTY, BioPortalConstants.DEFAULT_API_KEY);
    }

    @Override
    public InputStream doCall() throws IOException {
        addAPIKey();
        return super.doCall();
    }
}
