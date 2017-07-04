package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jun 2017
 */
public class LiteralSerializer extends StdSerializer<OWLLiteral> {

    public LiteralSerializer() {
        super(OWLLiteral.class);
    }

    @Override
    public void serialize(OWLLiteral value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("literal", value.getLiteral());
        if (value.isRDFPlainLiteral()) {
            gen.writeStringField("lang", value.getLang());
        }
        else {
            gen.writeStringField("datatype", value.getDatatype().getIRI().toString());
        }
        gen.writeEndObject();
    }
}
