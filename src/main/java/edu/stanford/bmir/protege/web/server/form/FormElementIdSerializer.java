package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class FormElementIdSerializer extends StdSerializer<FormElementId> {

    public FormElementIdSerializer() {
        super(FormElementId.class);
    }

    @Override
    public void serialize(FormElementId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getId());
    }
}
