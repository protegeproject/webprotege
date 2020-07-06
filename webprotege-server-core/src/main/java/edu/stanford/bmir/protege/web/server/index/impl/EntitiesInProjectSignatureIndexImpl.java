package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
@ProjectSingleton
public class EntitiesInProjectSignatureIndexImpl implements EntitiesInProjectSignatureIndex, DependentIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final EntitiesInOntologySignatureIndex entitiesInOntologySignatureIndex;

    @Inject
    public EntitiesInProjectSignatureIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                               @Nonnull EntitiesInOntologySignatureIndex entitiesInOntologySignatureIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.entitiesInOntologySignatureIndex = checkNotNull(entitiesInOntologySignatureIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex, entitiesInOntologySignatureIndex);
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        return projectOntologiesIndex.getOntologyIds()
                .anyMatch(ontId -> entitiesInOntologySignatureIndex.containsEntityInSignature(entity, ontId));

    }
}
