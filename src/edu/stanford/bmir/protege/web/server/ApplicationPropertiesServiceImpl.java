package edu.stanford.bmir.protege.web.server;

import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.rpc.ApplicationPropertiesService;

/**
 * Administrative services for user management
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class ApplicationPropertiesServiceImpl extends WebProtegeRemoteServiceServlet implements ApplicationPropertiesService {

    private static final long serialVersionUID = -2917459604045537935L;

    public Map<String, String> initialize() {
        return WebProtegeProperties.getPropertiesForClient();
    }
}