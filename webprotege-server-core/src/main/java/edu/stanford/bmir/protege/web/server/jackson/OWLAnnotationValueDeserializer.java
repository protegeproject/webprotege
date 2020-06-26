package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class OWLAnnotationValueDeserializer extends StdDeserializer<OWLAnnotationValue> {

    @Nonnull
    private final OWLLiteralDeserializer literalDeserializer;

    @Nonnull
    private final IriDeserializer iriDeserializer;

    @Inject
    @Nonnull
    public OWLAnnotationValueDeserializer(@Nonnull OWLLiteralDeserializer literalDeserializer, @Nonnull IriDeserializer iriDeserializer) {
        super(OWLAnnotationValue.class);
        this.literalDeserializer = checkNotNull(literalDeserializer);
        this.iriDeserializer = checkNotNull(iriDeserializer);
    }

    @Override
    public OWLAnnotationValue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if(p.hasToken(JsonToken.START_OBJECT)) {
            return literalDeserializer.deserialize(p, ctxt);
        }
        else {
            return iriDeserializer.deserialize(p, ctxt);
        }
    }
}

