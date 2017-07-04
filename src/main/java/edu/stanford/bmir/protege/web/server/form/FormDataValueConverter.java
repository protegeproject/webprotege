package edu.stanford.bmir.protege.web.server.form;

import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.server.persistence.OWLEntityConverter;
import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import org.bson.Document;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.inject.Inject;
import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2017
 */
public class FormDataValueConverter extends TypeSafeConverter<Object, FormDataValue> implements SimpleValueConverter {

    private final OWLDataFactory df;

    private final OWLEntityConverter entityConverter;

    @Inject
    public FormDataValueConverter(OWLDataFactory dataFactory, OWLEntityConverter entityConverter) {
        this.df = dataFactory;
        this.entityConverter = entityConverter;
        setSupportedTypes(null);
    }

    @Override
    protected boolean isSupported(Class<?> c, MappedField optionalExtraInfo) {
        return FormDataValue.class.isAssignableFrom(c);
    }

    @Override
    public FormDataValue decodeObject(Object fromDBObject, MappedField optionalExtraInfo) {
        if(fromDBObject instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) fromDBObject;
            if(map.size() == 2 && map.containsKey("literal") && map.containsKey("lang")) {
                return FormDataPrimitive.get(map.get("literal").toString(),
                                      map.get("lang").toString());
            }
            else if(map.size() == 2 && map.containsKey("literal") && map.containsKey("datatype")) {
                OWLLiteral literal = df.getOWLLiteral(map.get("literal").toString(),
                                                      df.getOWLDatatype(IRI.create(map.get("datatype").toString())));
                return FormDataPrimitive.get(literal);
            }
            else if(map.size() == 2 && map.containsKey("iri") && map.containsKey("type")) {
                OWLEntity entity = entityConverter.decodeObject((DBObject) map, optionalExtraInfo);
                return FormDataPrimitive.get(entity);
            }
            else if(map.size() == 1 && map.containsKey("iri")) {
                return FormDataPrimitive.get(IRI.create(map.get("iri").toString()));
            }
            else {
                Map<String, FormDataValue> result = new HashMap<>();
                map.forEach((key, val) -> {
                    result.put(key, decodeObject(val, optionalExtraInfo));
                });
                return new FormDataObject(result);
            }

        }
        else if(fromDBObject instanceof List) {
            List<FormDataValue> result = new ArrayList<>();
            for(Object o : (List) fromDBObject) {
                FormDataValue value = decodeObject(o, optionalExtraInfo);
                result.add(value);
            }
            return new FormDataList(result);

        }
        else if(fromDBObject instanceof String) {
            return FormDataPrimitive.get((String) fromDBObject);
        }
        else if(fromDBObject instanceof Boolean) {
            return FormDataPrimitive.get((Boolean) fromDBObject);
        }
        else if(fromDBObject instanceof Number) {
            return FormDataPrimitive.get((Number) fromDBObject);
        }
        else {
            throw new RuntimeException("Unknow type of object " + fromDBObject);
        }
    }

    @Override
    public Object encodeObject(FormDataValue value, MappedField optionalExtraInfo) {
        if(value instanceof FormDataObject) {
            Document document = new Document();
            FormDataObject object = (FormDataObject) value;
            object.getMap().forEach((key, val) -> {
                document.put(key, encodeObject(val, optionalExtraInfo));
            });
            return document;
        }
        else if(value instanceof FormDataList) {
            List<Object> result = new ArrayList<>();
            FormDataList list = (FormDataList) value;
            list.getList().forEach(e -> result.add(encodeObject(e, optionalExtraInfo)));
            return result;
        }
        else if(value instanceof FormDataPrimitive) {
            FormDataPrimitive primitive = (FormDataPrimitive) value;
            if(primitive.isNumber()) {
               return primitive.getValueAsDouble();
            }
            else if(primitive.isString()) {
                return primitive.getValueAsString();
            }
            else if(primitive.isBoolean()) {
                return primitive.getValueAsBoolean();
            }
            else if(primitive instanceof FormDataPrimitive.LiteralPrimitive) {
                OWLLiteral literal = primitive.asLiteral().get();
                Document document = new Document();
                document.put("literal", literal.getLiteral());
                if(literal.isRDFPlainLiteral()) {
                    document.put("lang", literal.getLang());
                }
                else {
                    document.put("datatype", literal.getDatatype());
                }
                return document;
            }
            else if(primitive instanceof FormDataPrimitive.IRIPrimitive) {
                return new Document("iri", primitive.asIRI().get().toString());
            }
            else if(primitive instanceof FormDataPrimitive.OWLEntityPrimitive) {
                OWLEntity entity = (OWLEntity) primitive.getValue();
                return entityConverter.encodeObject(entity, optionalExtraInfo);
            }
        }
        else {
            throw new RuntimeException("Unknown type of object " + value);
        }
        return null;
    }
}
