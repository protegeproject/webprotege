package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.EntitiesInOntologySignatureIndex;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
public class EntitiesInProjectSignatureIndexImpl implements EntitiesInProjectSignatureIndex {

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

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        return projectOntologiesIndex.getOntologyIds()
                .anyMatch(ontId -> entitiesInOntologySignatureIndex.containsEntityInSignature(entity, ontId));

    }
}
