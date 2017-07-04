package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;

import java.io.IOException;
import java.lang.reflect.Type;

import static cern.clhep.Units.g;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class FormDataPrimitiveSerializer extends StdSerializer<FormDataPrimitive> {

    public FormDataPrimitiveSerializer() {
        super(FormDataPrimitive.class);
    }

    @Override
    public void serialize(FormDataPrimitive value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if(value.isString()) {
            gen.writeString(value.getValueAsString());
        }
        else if(value.isNumber()) {
            gen.writeNumber(value.getValueAsDouble());
        }
        else if(value.isBoolean()) {
            gen.writeBoolean(value.getValueAsBoolean());
        }
        else {
            gen.writeObject(value.getValue());
        }
    }
}
