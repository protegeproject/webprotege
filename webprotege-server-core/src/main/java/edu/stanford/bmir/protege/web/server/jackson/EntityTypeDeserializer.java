package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.semanticweb.owlapi.model.EntityType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class EntityTypeDeserializer extends StdDeserializer<EntityType<?>> {

    public EntityTypeDeserializer() {
        super(EntityType.class);
    }

    @Override
    public EntityType<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String typeName = jsonParser.getValueAsString();
        if(EntityType.CLASS.getPrefixedName().equals(typeName)) {
            return EntityType.CLASS;
        }
        else if(EntityType.OBJECT_PROPERTY.getPrefixedName().equals(typeName)) {
            return EntityType.OBJECT_PROPERTY;
        }
        else if(EntityType.DATA_PROPERTY.getPrefixedName().equals(typeName)) {
            return EntityType.DATA_PROPERTY;
        }
        else if(EntityType.ANNOTATION_PROPERTY.getPrefixedName().equals(typeName)) {
            return EntityType.ANNOTATION_PROPERTY;
        }
        else if(EntityType.NAMED_INDIVIDUAL.getPrefixedName().equals(typeName)) {
            return EntityType.NAMED_INDIVIDUAL;
        }
        else if(EntityType.DATATYPE.getPrefixedName().equals(typeName)) {
            return EntityType.DATATYPE;
        }
        throw new IOException("Unrecognized entity type name: " + typeName);
    }
}
