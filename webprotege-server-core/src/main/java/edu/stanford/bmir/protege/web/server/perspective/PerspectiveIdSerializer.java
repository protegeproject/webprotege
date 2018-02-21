package edu.stanford.bmir.protege.web.server.perspective;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.lang.reflect.Type;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/16
 */
public class PerspectiveIdSerializer implements JsonSerializer<PerspectiveId> {

    @Override
    public JsonElement serialize(PerspectiveId perspectiveId, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(perspectiveId.getId());
    }
}
