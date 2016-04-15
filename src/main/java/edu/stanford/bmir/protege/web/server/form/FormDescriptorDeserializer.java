package edu.stanford.bmir.protege.web.server.form;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class FormDescriptorDeserializer implements JsonDeserializer<FormDescriptor> {

    @Override
    public FormDescriptor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(!jsonElement.isJsonObject()) {
            throw createParseException(jsonElement, "Object");
        }
        JsonObject object = jsonElement.getAsJsonObject();
        String id = object.getAsJsonPrimitive("id").getAsString();
        JsonArray fieldsArray = object.getAsJsonArray("fields");
        List<FormElementDescriptor> formElementDescriptors = new ArrayList<>();
        for(JsonElement fieldElement : fieldsArray) {
            FormElementDescriptor elementDescriptor = jsonDeserializationContext.deserialize(fieldElement, FormElementDescriptor.class);
            formElementDescriptors.add(elementDescriptor);
        }
        return new FormDescriptor(new FormId(id), formElementDescriptors);
    }

    private static FormDescriptorParseException createParseException(JsonElement element, String typeName) {
        return new FormDescriptorParseException(
                String.format("Encountered %s but expected Json %s", element, typeName)
        );
    }
}
