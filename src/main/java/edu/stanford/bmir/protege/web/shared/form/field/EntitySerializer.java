package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jun 2017
 */
public class EntitySerializer extends StdSerializer<OWLEntity> {

    public EntitySerializer() {
        super(OWLEntity.class);
    }

    @Override
    public void serialize(OWLEntity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", value.getEntityType().getName());
        gen.writeStringField("iri", value.getIRI().toString());
        gen.writeEndObject();
    }
}
