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
        return ApplicationPropertyDefaults.SERVER_POLLING_TIMEOUT_MINUTES_DEFAULT;
    }

    public static String getApplicationHttpsPort() {
        final String o = cache.get(ApplicationPropertyNames.HTTPS_PORT);
        return o;
    }

    public static Boolean getWebProtegeAuthenticateWithOpenId() {
        final String o = cache.get(ApplicationPropertyNames.OPEN_ID_ENABLED);
        return o == null ? Boolean.FALSE : Boolean.parseBoolean(o);
    }

    public static Boolean getLoginWithHttps() {
        final String o = cache.get(ApplicationPropertyNames.HTTPS_ENABLED);
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
