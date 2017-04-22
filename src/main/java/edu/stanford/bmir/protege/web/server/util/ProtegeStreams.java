package edu.stanford.bmir.protege.web.server.util;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

import java.util.Set;
import java.util.stream.Stream;

import static org.semanticweb.owlapi.model.EntityType.*;
import static org.semanticweb.owlapi.model.EntityType.DATATYPE;
import static org.semanticweb.owlapi.model.EntityType.NAMED_INDIVIDUAL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class ProtegeStreams {

    public static Stream<OWLEntity> entityStream(Set<EntityType<?>> entityTypes, OWLOntology ontology, Imports imports) {
        Stream<OWLOntology> ontologyStream = ontologyStream(ontology, imports);
        return ontologyStream.flatMap(o -> {
            Stream<OWLEntity> entityStream = Stream.empty();
            if (entityTypes.contains(CLASS)) {
                entityStream = Stream.concat(entityStream,
                                             o.getClassesInSignature()
                                              .stream());
            }
            if (entityTypes.contains(OBJECT_PROPERTY)) {
                entityStream = Stream.concat(entityStream,
                                             o.getObjectPropertiesInSignature()
                                              .stream());
            }
            if (entityTypes.contains(DATA_PROPERTY)) {
                entityStream = Stream.concat(entityStream,
                                             o.getDataPropertiesInSignature()
                                              .stream());
            }
            if (entityTypes.contains(ANNOTATION_PROPERTY)) {
                entityStream = Stream.concat(entityStream,
                                             o.getAnnotationPropertiesInSignature()
                                              .stream());
            }
            if (entityTypes.contains(NAMED_INDIVIDUAL)) {
                entityStream = Stream.concat(entityStream,
                                             o.getIndividualsInSignature()
                                              .stream());
            }
            if (entityTypes.contains(DATATYPE)) {
                entityStream = Stream.concat(entityStream,
                                             o.getDatatypesInSignature()
                                              .stream());
            }
            return entityStream;
        });
    }

    public static Stream<OWLOntology> ontologyStream(OWLOntology o, Imports imports) {
        Stream<OWLOntology> ontologyStream;
        if(imports == Imports.INCLUDED) {
            ontologyStream = o.getImportsClosure().stream();
        }
        else {
            ontologyStream = Stream.of(o);
        }
        return ontologyStream;
    }
}
