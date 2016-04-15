package edu.stanford.bmir.protege.web.server.form;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import java.lang.reflect.Type;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class FormIdSerializer implements JsonSerializer<FormId> {

    @Override
    public JsonElement serialize(FormId formId, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(formId.getId());
    }
}
