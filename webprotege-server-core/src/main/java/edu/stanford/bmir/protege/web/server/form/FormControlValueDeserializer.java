package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Jul 2017
 */
public class FormControlValueDeserializer extends StdDeserializer<PrimitiveFormControlData> {

    private final OWLDataFactory df;

    public FormControlValueDeserializer(OWLDataFactory df) {
        super(FormControlData.class);
        this.df = checkNotNull(df);
    }

    @Override
    public PrimitiveFormControlData deserialize(JsonParser p,
                                       DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode node = p.readValueAsTree();
        if(node.isTextual()) {
            return PrimitiveFormControlData.get(node.asText());
        }
        else if(node.isNumber()) {
            return PrimitiveFormControlData.get(node.asDouble());
        }
        else if(node.isBoolean()) {
            return PrimitiveFormControlData.get(node.asBoolean());
        }
        else if(node.isObject()) {
            if(node.has("iri")) {
                if(node.has("type")) {
                    IRI iri = IRI.create(node.get("iri").asText());
                    String type = node.get("type").asText();
                    switch (type) {
                        case "owl:Class" :
                            return PrimitiveFormControlData.get(df.getOWLClass(iri));
                        case "owl:ObjectProperty":
                            return PrimitiveFormControlData.get(df.getOWLObjectProperty(iri));
                        case "owl:DataProperty" :
                            return PrimitiveFormControlData.get(df.getOWLDataProperty(iri));
                        case "owl:AnnotationProperty":
                            return PrimitiveFormControlData.get(df.getOWLAnnotationProperty(iri));
                        case "owl:Datatype" :
                            return PrimitiveFormControlData.get(df.getOWLDatatype(iri));
                        case "owl:NamedIndividual" :
                            return PrimitiveFormControlData.get(df.getOWLDatatype(iri));
                    }
                    throw new JsonParseException(p, "Unrecognised entity type: " + type);
                }
                else {
                    return PrimitiveFormControlData.get(IRI.create(node.get("iri").asText()));
                }
            }
            // literal supported for legacy reasons
            else if(node.has("value") || node.has("literal")) {
                JsonNode literalNode = node.get("value");
                if(literalNode == null) {
                    literalNode = node.get("literal");
                }
                String literal = literalNode.asText();
                JsonNode langNode = node.get("lang");
                JsonNode typeNode = node.get("type");
                if(typeNode != null && !typeNode.asText().equals(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI().toString())) {
                    return PrimitiveFormControlData.get(df.getOWLLiteral(literal, df.getOWLDatatype(IRI.create(typeNode.asText()))));
                }
                else {
                    return PrimitiveFormControlData.get(df.getOWLLiteral(literal, langNode.asText("")));
                }

            }
        }
        throw new JsonParseException(p, "Cannot parse node as primitive value");
    }
}
