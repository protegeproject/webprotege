package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class OWLClassDeserializer extends StdDeserializer<OWLClass> {

    private OWLEntityDeserializer deserializer;

    public OWLClassDeserializer(OWLDataFactory dataFactory) {
        super(OWLClass.class);
        deserializer = new OWLEntityDeserializer(dataFactory);
    }

    @Override
    public OWLClass deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        OWLEntity entity = deserializer.deserialize(jsonParser, deserializationContext);
        if(!entity.isOWLClass()) {
            throw new JsonParseException(jsonParser,
                                         "Expected an owl:Class but found an "
                                                 + entity.getEntityType().getPrefixedName());
        }
        return (OWLClass) entity;
    }
}
