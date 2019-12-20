package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class OWLPropertyDeserializer extends StdDeserializer<OWLProperty> {

    @Nonnull
    private final OWLEntityDeserializer deserializer;

    public OWLPropertyDeserializer(@Nonnull OWLDataFactory dataFactory) {
        super(OWLProperty.class);
        deserializer = new OWLEntityDeserializer(dataFactory);
    }

    @Override
    public OWLProperty deserialize(JsonParser jsonParser,
                                   DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        OWLEntity entity = deserializer.deserialize(jsonParser, deserializationContext);
        if(!(entity.isOWLObjectProperty() || entity.isOWLDataProperty() || entity.isOWLAnnotationProperty())) {
            throw new JsonParseException(jsonParser,
                                         "Expected an owl:ObjectProperty, owl:DataProperty or owl:AnnotationProperty but found an "
                                                 + entity.getEntityType().getPrefixedName());
        }
        return (OWLProperty) entity;
    }
}
