package edu.stanford.bmir.protege.web.server.index;

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
    private final OntologyIndex ontologyIndex;

    @Inject
    public EntitiesInProjectSignatureIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                               @Nonnull OntologyIndex ontologyIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        return projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> ontologyIndex.getOntology(ontId).stream())
                .anyMatch(ont -> ont.containsEntityInSignature(entity));

    }
}
