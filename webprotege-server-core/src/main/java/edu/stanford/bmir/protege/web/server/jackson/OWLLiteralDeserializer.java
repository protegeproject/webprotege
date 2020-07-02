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
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class OWLLiteralDeserializer extends StdDeserializer<OWLLiteral> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    public OWLLiteralDeserializer(@Nonnull OWLDataFactory dataFactory) {
        super(OWLLiteral.class);
        this.dataFactory = dataFactory;
    }

    @Override
    public OWLLiteral deserialize(JsonParser jsonParser,
                                  DeserializationContext ctxt) throws IOException, JsonProcessingException {

        String value = null;
        String lang = null;
        IRI iri = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = jsonParser.getCurrentName();
            System.out.println(fieldname);
            if ("lang".equals(fieldname)) {
                jsonParser.nextToken();
                lang = jsonParser.readValueAs(String.class);
            }
            else if("value".equals(fieldname)) {
                jsonParser.nextToken();
                value = jsonParser.readValueAs(String.class);
            }
            else if("type".equals(fieldname)) {
                jsonParser.nextToken();
                iri = jsonParser.readValueAs(IRI.class);
            }
            if(fieldname == null) {
                break;
            }
        }
        if(value == null) {
            throw new JsonParseException(jsonParser, "value field is missing");
        }
        if(lang != null) {
            return dataFactory.getOWLLiteral(value, lang);
        }
        if(iri != null) {
            return dataFactory.getOWLLiteral(value, dataFactory.getOWLDatatype(iri));
        }
        return dataFactory.getOWLLiteral(value, "");
    }
}
