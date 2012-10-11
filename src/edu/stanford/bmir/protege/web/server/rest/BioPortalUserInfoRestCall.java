package edu.stanford.bmir.protege.web.server.rest;

import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;
import edu.stanford.bmir.protege.web.server.bioportal.BioPortalUserInfoBean;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalUserInfoRestCall extends BioPortalRestCall<BioPortalUserInfoBean> {

    public static final String AUTH_REST_SERVICE_CALL_NAME = "auth";

    public static final String USERNAME_PARAMETER_NAME = "username";

    public static final String PASSWORD_PARAMETER_NAME = "password";

    public BioPortalUserInfoRestCall(String username, String userpassword) {
        this(BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL, username, userpassword);
        
    }

    public BioPortalUserInfoRestCall(String restServiceBase, String username, String userpassword) {
        super(restServiceBase, AUTH_REST_SERVICE_CALL_NAME, BioPortalUserInfoBean.class);
        addParameterValue(USERNAME_PARAMETER_NAME, username);
        addParameterValue(PASSWORD_PARAMETER_NAME, userpassword);
    }
}
