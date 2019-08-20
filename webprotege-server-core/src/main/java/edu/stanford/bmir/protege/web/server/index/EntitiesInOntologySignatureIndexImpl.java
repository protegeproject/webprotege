package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
public class EntitiesInOntologySignatureIndexImpl implements EntitiesInOntologySignatureIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public EntitiesInOntologySignatureIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity entity, @Nonnull OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId)
                            .stream()
                            .anyMatch(ont -> ont.containsEntityInSignature(entity));
    }
}
