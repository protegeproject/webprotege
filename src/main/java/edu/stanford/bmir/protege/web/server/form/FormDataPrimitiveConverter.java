package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import org.bson.Document;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2017
 */
public class FormDataPrimitiveConverter extends TypeSafeConverter<Object, FormDataPrimitive>  implements SimpleValueConverter {

    private OWLDataFactory df;

    @Inject
    public FormDataPrimitiveConverter(OWLDataFactory df) {
        this.df = df;
        setSupportedTypes(null);
    }


    @Override
    protected boolean isSupported(Class<?> c, MappedField optionalExtraInfo) {
        // Anything that is a
        return FormDataPrimitive.class.isAssignableFrom(c);
    }

    @Override
    public FormDataPrimitive decodeObject(Object object, MappedField optionalExtraInfo) {
        if(object instanceof String) {
            return FormDataPrimitive.get((String) object);
        }
        else if(object instanceof Number) {
            return FormDataPrimitive.get((Number) object);
        }
        else if(object instanceof Boolean) {
            return FormDataPrimitive.get((Boolean) object);
        }
        else if(object instanceof DBObject) {
            Document dbObject = (Document) object;
            if(dbObject.containsKey("iri")) {
                if(dbObject.containsKey("type")) {
                    IRI iri = IRI.create(dbObject.getString("iri"));
                    String type = dbObject.getString("type");
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
                    throw new RuntimeException("Unrecognised entity type: " + type);
                }
                else {
                    return FormDataPrimitive.get(IRI.create(dbObject.getString("iri")));
                }
            }
            else if(dbObject.containsKey("literal")) {
                String literal = dbObject.getString("literal");
                String lang = dbObject.getString("lang");
                return FormDataPrimitive.get(df.getOWLLiteral(literal, lang));
            }
        }
        throw new RuntimeException("Cannot parse node as primitive value");
    }

    @Override
    public Object encodeObject(FormDataPrimitive value, MappedField optionalExtraInfo) {
        if(value.isString()) {
            return value.getValueAsString();
        }
        else if(value.isNumber()) {
            return value.getValueAsDouble();
        }
        else if(value.isBoolean()) {
            return value.getValueAsBoolean();
        }
        else {
            return value.getValue();
        }
    }
}
