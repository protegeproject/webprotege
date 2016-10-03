package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.DBObject;
import org.semanticweb.owlapi.model.*;

import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryInternalsImplNoCache;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class OWLEntityReadConverter implements Converter<DBObject, OWLEntity> {

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl(new OWLDataFactoryInternalsImplNoCache(false));

    @Override
    public OWLEntity convert(DBObject object) {
        Object typeObject = object.get("type");
        if(typeObject == null) {
            throw new IllegalArgumentException("Missing type property value");
        }
        Object iriObject = object.get("iri");
        if(iriObject == null) {
            throw new IllegalArgumentException("Missing iri property value");
        }

        String typeString = typeObject.toString();
        IRI iri = IRI.create(iriObject.toString());
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
}
