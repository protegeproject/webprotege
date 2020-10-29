package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

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
                                                        @Nonnull OntologyDocumentId ontologyDocumentId) {
        checkNotNull(type);
        checkNotNull(ontologyDocumentId);
        if(type.equals(EntityType.ANNOTATION_PROPERTY)) {
            return Stream.<E>concat(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(type, ontologyDocumentId),
                                    (Stream<E>) ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(
                                            ontologyDocumentId));
        }
        else {
            return ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(type, ontologyDocumentId);
        }
    }
}
