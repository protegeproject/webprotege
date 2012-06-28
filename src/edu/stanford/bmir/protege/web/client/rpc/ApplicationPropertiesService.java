package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Map;

/**
 *
 * @author Jack Elliott <jack.elliott@stanford.edu>
 */
@RemoteServiceRelativePath("applicationProperties")
public interface ApplicationPropertiesService extends RemoteService {

    Map<String, String> initialize();    

}