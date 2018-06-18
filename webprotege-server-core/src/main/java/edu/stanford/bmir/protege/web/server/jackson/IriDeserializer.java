package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.semanticweb.owlapi.model.IRI;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class IriDeserializer extends StdDeserializer<IRI> {

    public IriDeserializer() {
        super(IRI.class);
    }

    @Override
    public IRI deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return IRI.create(jsonParser.getValueAsString());
    }
}

