package edu.stanford.bmir.protege.web.server.app;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/12/14
 */
public class ClientApplicationPropertiesEncoder implements ClientObjectEncoder<ClientApplicationProperties> {

    @Override
    public JsonObject encode(ClientApplicationProperties properties) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for(WebProtegePropertyName propertyName : WebProtegePropertyName.values()) {
            Optional<String> propertyValue = properties.getPropertyValue(propertyName);
            if (propertyValue.isPresent()) {
                builder.add(propertyName.getPropertyName(), propertyValue.get());
            }
        }
        return builder.build();
    }
}
