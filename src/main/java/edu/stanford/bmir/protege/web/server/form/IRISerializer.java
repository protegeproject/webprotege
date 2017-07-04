package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.semanticweb.owlapi.model.IRI;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jun 2017
 */
public class IRISerializer extends StdSerializer<IRI> {

    public IRISerializer() {
        super(IRI.class);
    }

    @Override
    public void serialize(IRI value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("iri", value.toString());
        gen.writeEndObject();
    }
}
