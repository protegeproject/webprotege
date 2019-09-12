package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsSignatureIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsSignatureIndex;
import edu.stanford.bmir.protege.web.server.index.OntologySignatureByTypeIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@ProjectSingleton
public class OntologySignatureByTypeIndexImpl implements OntologySignatureByTypeIndex, DependentIndex {

    @Nonnull
    private final OntologyAxiomsSignatureIndex ontologyAxiomsSignatureIndex;

    @Nonnull
    private final OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @Inject
    public OntologySignatureByTypeIndexImpl(@Nonnull OntologyAxiomsSignatureIndex ontologyAxiomsSignatureIndex,
                                            @Nonnull OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex) {
        this.ontologyAxiomsSignatureIndex = checkNotNull(ontologyAxiomsSignatureIndex);
        this.ontologyAnnotationsSignatureIndex = checkNotNull(ontologyAnnotationsSignatureIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(ontologyAnnotationsSignatureIndex, ontologyAxiomsSignatureIndex);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <E extends OWLEntity> Stream<E> getSignature(@Nonnull EntityType<E> type,
                                                        @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(type);
        checkNotNull(ontologyId);
        if(type.equals(EntityType.ANNOTATION_PROPERTY)) {
            return Stream.<E>concat(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(type, ontologyId),
                                    (Stream<E>) ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(ontologyId));
        }
        else {
            return ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(type, ontologyId);
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends OWLEntity> Stream<? extends E> getStreamOfType(@Nonnull EntityType<E> type,
                                                                      @Nonnull OWLOntology ont) {
        if(type.equals(EntityType.CLASS)) {
            return (Stream<E>) ont.getClassesInSignature().stream();
        }
        if(type.equals(EntityType.DATATYPE)) {
            return (Stream<E>) ont.getDatatypesInSignature().stream();
        }
        if(type.equals(EntityType.OBJECT_PROPERTY)) {
            return (Stream<E>) ont.getObjectPropertiesInSignature().stream();
        }
        if(type.equals(EntityType.DATA_PROPERTY)) {
            return (Stream<E>) ont.getDataPropertiesInSignature().stream();
        }
        if(type.equals(EntityType.ANNOTATION_PROPERTY)) {
            return (Stream<E>) ont.getAnnotationPropertiesInSignature().stream();
        }
        if(type.equals(EntityType.NAMED_INDIVIDUAL)) {
            return (Stream<E>) ont.getIndividualsInSignature().stream();
        }
        throw new RuntimeException("Unknown Entity Type: " + type);
    }
}
