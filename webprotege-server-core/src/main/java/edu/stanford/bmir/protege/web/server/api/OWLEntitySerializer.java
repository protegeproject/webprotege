package edu.stanford.bmir.protege.web.server.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 */
public class OWLEntitySerializer extends StdSerializer<OWLEntity> {

    public OWLEntitySerializer() {
        super(OWLEntity.class);
    }

    @Override
    public void serialize(OWLEntity entity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("type");
        jsonGenerator.writeString(entity.getEntityType().getName());
        jsonGenerator.writeFieldName("iri");
        jsonGenerator.writeString(entity.getIRI().toQuotedString());
        jsonGenerator.writeEndObject();
    }
}
