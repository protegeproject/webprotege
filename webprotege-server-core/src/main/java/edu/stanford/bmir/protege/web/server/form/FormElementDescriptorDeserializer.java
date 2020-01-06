package edu.stanford.bmir.protege.web.server.form;

import com.google.gson.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import java.lang.reflect.Type;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class FormElementDescriptorDeserializer implements JsonDeserializer<FormFieldDescriptor> {

    @Override
    public FormFieldDescriptor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//        if(!jsonElement.isJsonObject()) {
//            throw new FormDescriptorParseException("Found " + jsonElement + " but expected Object");
//        }
//        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        try {
//            String id = jsonObject.getAsJsonPrimitive("id").getAsString();
//            String label = jsonObject.getAsJsonPrimitive("label").getAsString();
//            String repeatabilityValue = jsonObject.getAsJsonPrimitive("repeatability").getAsString();
//            String requiredValue = jsonObject.getAsJsonObject("required").getAsString();
//            Repeatability repeatability = Repeatability.valueOf(repeatabilityValue);
//            Optionality required = Optionality.valueOf(requiredValue);
//            FormControlDescriptor fieldDescriptor = jsonDeserializationContext.deserialize(jsonObject.getAsJsonObject("fieldDescriptor"), FormControlDescriptor.class);
//            return new FormFieldDescriptor(FormFieldId.get(id), label, fieldDescriptor, repeatability, required, "");
//        } catch (IllegalArgumentException e) {
//            throw new FormDescriptorParseException("Encountered "
//                    + jsonObject.getAsJsonPrimitive("repeatability").getAsString()
//                    + " for the repeatability value.  Expected one of NON_REPEATABLE, REPEATABLE_HORIZONTALLY, REPEATABLE_VERTICALLY");
//        }
        throw new RuntimeException();
    }
}
