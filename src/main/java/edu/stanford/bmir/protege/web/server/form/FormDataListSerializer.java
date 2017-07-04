package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jun 2017
 */
public class FormDataListSerializer extends StdSerializer<FormDataList> {

    public FormDataListSerializer() {
        super(FormDataList.class);
    }

    @Override
    public void serialize(FormDataList value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartArray();
        for(FormDataValue val : value.getList()) {
            gen.writeObject(val);
        }
        gen.writeEndArray();
    }
}
