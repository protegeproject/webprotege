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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class OWLPropertyDeserializer<P extends OWLProperty> extends StdDeserializer<P> {

    @Nonnull
    private final OWLEntityDeserializer deserializer;

    @Nonnull
    private final Class<P> cls;

    public OWLPropertyDeserializer(@Nonnull OWLDataFactory dataFactory,
                                   @Nonnull Class<P> cls) {
        super(OWLProperty.class);
        deserializer = new OWLEntityDeserializer(dataFactory, cls);
        this.cls = checkNotNull(cls);
    }

    @Override
    public P deserialize(JsonParser jsonParser,
                                   DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        OWLEntity entity = deserializer.deserialize(jsonParser, deserializationContext);
        if(!(cls.isInstance(entity))) {
            throw new JsonParseException(jsonParser,
                                         "Expected " + cls.getSimpleName() + " but found "
                                                 + entity.getEntityType().getPrefixedName());
        }
        return cls.cast(entity);
    }
}
