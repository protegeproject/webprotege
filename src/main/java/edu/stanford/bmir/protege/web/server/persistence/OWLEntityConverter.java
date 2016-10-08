package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.server.inject.Application;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 *
 * {@link SimpleValueConverter} converts objects that are simple values.  As far as I can tell, these are values that
 * are not collections (arrays, lists, sets etc.).  This appears to be in contradition to the docs on {@link SimpleValueConverter}.
 */
public class OWLEntityConverter extends TypeSafeConverter<DBObject, OWLEntity> implements SimpleValueConverter {

    private static final Logger logger = LoggerFactory.getLogger(OWLEntityConverter.class);

    private static final String TYPE_PROPERTY_NAME = "type";

    private static final String IRI_PROPERTY_NAME = "iri";

    @Nonnull
    private final OWLEntityProvider dataFactory;

    @Inject
    public OWLEntityConverter(@Nonnull @Application OWLEntityProvider dataFactory) {
        this.dataFactory = checkNotNull(dataFactory);
        // Don't specify a specific type or enumeration of types as we want to convert anything that is assignable
        // to OWLEntity.
        setSupportedTypes(null);
    }

    @Override
    protected boolean isSupported(Class<?> c, MappedField optionalExtraInfo) {
        // Anything that is an OWLEntity
        return OWLEntity.class.isAssignableFrom(c);
    }

    @Nullable
    @Override
    public OWLEntity decodeObject(DBObject document, MappedField optionalExtraInfo) {
        Object typeObject = document.get("type");
        if(typeObject == null) {
            logger.debug("Could not convert document to OWLEntity: Missing 'type' property value in {}", document);
            return null;
        }
        Object iriObject = document.get("iri");
        if(iriObject == null) {
            logger.debug("Could not convert document to OWLEntity: Missing 'iri' property value in {}", document);
            return null;
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
                logger.debug("Could not convert document to OWLEntity: Expected " +
                                     "Class, " +
                                     "ObjectProperty, " +
                                     "DataProperty, " +
                                     "AnnotationProperty, " +
                                     "NamedIndividual or " +
                                     "Datatype as a value for the 'type' property, but found {}.", typeString);
                return null;
        }
    }

    @Override
    public DBObject encodeObject(OWLEntity entity, MappedField optionalExtraInfo) {
        BasicDBObject document = new BasicDBObject();
        document.append(TYPE_PROPERTY_NAME, entity.getEntityType().getName());
        document.append(IRI_PROPERTY_NAME, entity.getIRI().toString());
        return document;
    }
}
