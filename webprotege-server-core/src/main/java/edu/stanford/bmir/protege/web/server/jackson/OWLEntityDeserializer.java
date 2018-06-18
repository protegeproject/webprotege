package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class OWLEntityDeserializer extends StdDeserializer<OWLEntity> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    public OWLEntityDeserializer(@Nonnull OWLDataFactory dataFactory) {
        super(OWLEntity.class);
        this.dataFactory = dataFactory;
    }

    @Override
    public OWLEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return deserialize(jsonParser);
    }

    protected OWLEntity deserialize(JsonParser jsonParser) throws IOException {
        EntityType<?> type = null;
        IRI iri = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = jsonParser.getCurrentName();
            if ("type".equals(fieldname)) {
                jsonParser.nextToken();
                type = jsonParser.readValueAs(EntityType.class);
            }
            else if("iri".equals(fieldname)) {
                jsonParser.nextToken();
                iri = jsonParser.readValueAs(IRI.class);
            }
        }
        if (type != null && iri != null) {
            return dataFactory.getOWLEntity(type, iri);
        }
        else {
            if(type == null) {
                throw new JsonParseException(jsonParser, "type field is missing");
            }
            else {
                throw new JsonParseException(jsonParser, "iri field is missing");
            }
        }
    }
}
