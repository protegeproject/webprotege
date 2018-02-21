package edu.stanford.bmir.protege.web.server.perspective;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.lang.reflect.Type;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/16
 */
public class PerspectiveIdDeserializer implements JsonDeserializer<PerspectiveId> {

    @Override
    public PerspectiveId deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new PerspectiveId(jsonElement.getAsString());
    }
}
