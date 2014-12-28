package edu.stanford.bmir.protege.web.client.app;

import com.google.common.base.Optional;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/12/14
 */
public class ClientApplicationPropertiesDecoder implements ClientObjectDecoder<ClientApplicationProperties> {

    public ClientApplicationPropertiesDecoder() {
    }

    @Override
    public ClientApplicationProperties decode(JSONValue v) {
        JSONObject object = v.isObject();
        ClientApplicationProperties.Builder builder = new ClientApplicationProperties.Builder();
        for(WebProtegePropertyName propertyName : WebProtegePropertyName.values()) {
            if(propertyName.isClientProperty()) {
                JSONValue propertyValue = object.get(propertyName.getPropertyName());
                if (propertyValue != null) {
                    builder.setPropertyValue(propertyName, propertyValue.toString());
                }
            }
        }
        return builder.build();
    }
}
