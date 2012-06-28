package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.ApplicationPropertiesServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ApplicationPropertyDefaults;
import edu.stanford.bmir.protege.web.client.rpc.data.ApplicationPropertyNames;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jack Elliott <jacke@stanford.edu>
 */
public class ClientApplicationPropertiesCache {
    private static Map<String, String> cache = new HashMap<String, String>();

    public static Integer getServerPollingTimeoutMinutes() {
        String o = cache.get(ApplicationPropertyNames.SERVER_POLLING_TIMEOUT_MINUTES_PROP);
        if (o == null){
            o = Integer.toString(ApplicationPropertyDefaults.SERVER_POLLING_TIMEOUT_MINUTES_DEFAULT);
        }
        return Integer.parseInt(o);
    }

    public static String getApplicationHttpsPort() {
        final String o = cache.get(ApplicationPropertyNames.APPLICATION_PORT_HTTPS_PROP);
        return o;
    }

    public static Boolean getWebProtegeAuthenticateWithOpenId() {
        final String o = cache.get(ApplicationPropertyNames.WEBPROTEGE_AUTHENTICATE_WITH_OPENID_PROP);
        return o == null ? Boolean.FALSE : Boolean.parseBoolean(o);
    }

    public static Boolean getLoginWithHttps() {
        final String o = cache.get(ApplicationPropertyNames.LOGIN_WITH_HTTPS_PROP);
        return o == null ? Boolean.FALSE : Boolean.parseBoolean(o);
    }

    public static void initialize(final AsyncCallback<Map<String, String>> callback){
        ApplicationPropertiesServiceManager.getInstance().initialize(new AsyncCallback<Map<String, String>>(){
            public void onFailure(Throwable caught) {
                GWT.log("Caught when trying to initialize client application properties.", caught);
                if (callback != null)
                    callback.onFailure(caught);
            }

            public void onSuccess(Map<String, String> result) {
                cache = result;
                if (callback != null)
                    callback.onSuccess(result);
            }
        });
    }
}
