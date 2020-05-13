package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class OWLEntityDeserializer<E extends OWLEntity> extends StdDeserializer<E> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final Class<E> cls;

    public OWLEntityDeserializer(@Nonnull OWLDataFactory dataFactory, @Nonnull Class<E> cls) {
        super(OWLEntity.class);
        this.dataFactory = dataFactory;
        this.cls = cls;
    }

    @Override
    public E deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return deserialize(jsonParser);
    }

    protected E deserialize(JsonParser jsonParser) throws IOException {
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
            OWLEntity owlEntity = dataFactory.getOWLEntity(type, iri);
            if (cls.isInstance(owlEntity)) {
                return cls.cast(owlEntity);
            }
            else {
                throw new JsonParseException(jsonParser, "Expected " + cls.getSimpleName() + " found " + owlEntity.getEntityType());
            }
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
