package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
public class ProjectSignatureByTypeIndexImpl implements ProjectSignatureByTypeIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OntologySignatureByTypeIndex ontologySignatureByTypeIndex;

    @Inject
    public ProjectSignatureByTypeIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                           @Nonnull OntologySignatureByTypeIndex ontologySignatureByTypeIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.ontologySignatureByTypeIndex = checkNotNull(ontologySignatureByTypeIndex);
    }

    @Nonnull
    @Override
    public <E extends OWLEntity> Stream<E> getSignature(@Nonnull EntityType<E> entityType) {
        checkNotNull(entityType);
        return projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> ontologySignatureByTypeIndex.getSignature(entityType, ontId));
    }
}
