package edu.stanford.bmir.protege.web.server.form;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;

import java.lang.reflect.Type;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class FormElementDescriptorSerializer implements JsonSerializer<FormElementDescriptor> {

    @Override
    public JsonElement serialize(FormElementDescriptor descriptor, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("id", descriptor.getId().getId());
        object.addProperty("label", descriptor.getLabel());
        object.addProperty("fieldType", descriptor.getFieldDescriptor().getAssociatedFieldTypeId());
        JsonElement fieldDescriptorSerialization = jsonSerializationContext.serialize(descriptor.getFieldDescriptor());
        object.add("fieldDescriptor", fieldDescriptorSerialization);
        object.addProperty("repeatability", descriptor.getRepeatability().name());
        object.addProperty("required", descriptor.getRequired().name());
        return object;
    }
}
