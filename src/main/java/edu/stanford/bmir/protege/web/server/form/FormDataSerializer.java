package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.stanford.bmir.protege.web.shared.form.FormData;

import java.io.IOException;

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
        value.getData().forEach((k, v) -> {
            try {
                gen.writeObjectField(k.getId(), v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        gen.writeEndObject();
    }
}
