package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import java.io.IOException;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Jun 2017
 */
public class FormDataSerializer extends StdSerializer<FormData> {

    public FormDataSerializer() {
        super(FormData.class);
    }

    @Override
    public void serialize(FormData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for(Map.Entry<FormElementId, FormDataValue> entry : value.getData().entrySet()) {
            gen.writeObjectField(entry.getKey().getId(), entry.getValue());
        }
        gen.writeEndObject();
    }
}
