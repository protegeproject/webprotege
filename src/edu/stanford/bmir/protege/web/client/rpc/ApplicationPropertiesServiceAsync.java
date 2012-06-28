package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

/**
 * @author Jack Elliott <jack.elliott@stanford.edu>
 */
public interface ApplicationPropertiesServiceAsync {

    void initialize(AsyncCallback<Map<String, String>> callback);
}