package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

import java.io.IOException;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jun 2017
 */
public class FormDataObjectSerializer extends StdSerializer<FormDataObject> {

    public FormDataObjectSerializer() {
        super(FormDataObject.class);
    }

    @Override
    public void serialize(FormDataObject value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for(Map.Entry<String, FormDataValue> entry : value.getMap().entrySet()) {
            gen.writeObjectField(entry.getKey(), entry.getValue());
        }
        gen.writeEndObject();
    }
}
