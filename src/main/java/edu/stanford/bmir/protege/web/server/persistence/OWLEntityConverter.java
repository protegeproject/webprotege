package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.BasicDBObject;
import edu.stanford.bmir.protege.web.server.inject.ApplicationDataFactory;
import org.bson.Document;
import org.mongodb.morphia.mapping.MappedField;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class OWLEntityConverter extends TypeSafeConverter<BasicDBObject, OWLEntity> {

    private static final String TYPE = "type";

    private static final String IRI = "iri";

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public OWLEntityConverter(@Nonnull @ApplicationDataFactory OWLDataFactory dataFactory) {
        super(OWLEntity.class);
        this.dataFactory = dataFactory;
    }

    @Override
    public OWLEntity decodeObject(BasicDBObject document, MappedField optionalExtraInfo) {
        Object typeObject = document.get("type");
        if(typeObject == null) {
            throw new IllegalArgumentException("Missing type property value");
        }
        Object iriObject = document.get("iri");
        if(iriObject == null) {
            throw new IllegalArgumentException("Missing iri property value");
        }

        String typeString = typeObject.toString();
        org.semanticweb.owlapi.model.IRI iri = org.semanticweb.owlapi.model.IRI.create(iriObject.toString());
        switch (typeString) {
            case "Class":
                return dataFactory.getOWLClass(iri);
            case "ObjectProperty":
                return dataFactory.getOWLObjectProperty(iri);
            case "DataProperty":
                return dataFactory.getOWLDataProperty(iri);
            case "AnnotationProperty":
                return dataFactory.getOWLAnnotationProperty(iri);
            case "NamedIndividual":
                return dataFactory.getOWLNamedIndividual(iri);
            case "Datatype":
                return dataFactory.getOWLDatatype(iri);
            default:
                throw new RuntimeException("Unknown entity type");
        }
    }

    @Override
    public BasicDBObject encodeObject(OWLEntity entity, MappedField optionalExtraInfo) {
        BasicDBObject document = new BasicDBObject();
        document.append(TYPE, entity.getEntityType().getName());
        document.append(IRI, entity.getIRI().toString());
        return document;
    }
}
