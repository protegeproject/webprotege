package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Jul 2017
 */
public class FormDataValueDeserializer extends StdDeserializer<FormDataValue> {

    private final OWLDataFactory df;

    public FormDataValueDeserializer(OWLDataFactory df) {
        super(FormDataValue.class);
        this.df = checkNotNull(df);
    }

    @Override
    public FormDataValue deserialize(JsonParser p,
                                     DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode node = p.readValueAsTree();
        if(node.isTextual()) {
            return FormDataPrimitive.get(node.asText());
        }
        else if(node.isNumber()) {
            return FormDataPrimitive.get(node.asDouble());
        }
        else if(node.isBoolean()) {
            return FormDataPrimitive.get(node.asBoolean());
        }
        else if(node.isObject()) {
            if(node.has("iri")) {
                if(node.has("type")) {
                    IRI iri = IRI.create(node.get("iri").asText());
                    String type = node.get("type").asText();
                    switch (type) {
                        case "Class" :
                            return FormDataPrimitive.get(df.getOWLClass(iri));
                        case "ObjectProperty":
                            return FormDataPrimitive.get(df.getOWLObjectProperty(iri));
                        case "DataProperty" :
                            return FormDataPrimitive.get(df.getOWLDataProperty(iri));
                        case "AnnotationProperty":
                            return FormDataPrimitive.get(df.getOWLAnnotationProperty(iri));
                        case "Datatype" :
                            return FormDataPrimitive.get(df.getOWLDatatype(iri));
                        case "NamedIndividual" :
                            return FormDataPrimitive.get(df.getOWLDatatype(iri));
                    }
                    throw new JsonParseException(p, "Unrecognised entity type: " + type);
                }
                else {
                    return FormDataPrimitive.get(IRI.create(node.get("iri").asText()));
                }
            }
            else if(node.has("literal")) {
                String literal = node.get("literal").asText();
                String lang = node.get("lang").asText("");
                return FormDataPrimitive.get(df.getOWLLiteral(literal, lang));
            }
        }
        throw new JsonParseException(p, "Cannot parse node as primitive value");
    }
}
