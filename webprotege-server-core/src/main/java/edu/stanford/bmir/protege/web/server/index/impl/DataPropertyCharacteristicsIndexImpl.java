package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.DataPropertyCharacteristicsIndex;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public class DataPropertyCharacteristicsIndexImpl implements DataPropertyCharacteristicsIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public DataPropertyCharacteristicsIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Override
    public boolean isFunctional(@Nonnull OWLDataProperty dataProperty,
                                @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(dataProperty);
        checkNotNull(ontologyId);
        return ontologyIndex.getOntology(ontologyId)
                .stream()
                .flatMap(ont -> ont.getFunctionalDataPropertyAxioms(dataProperty).stream())
                .findFirst()
                .isPresent();
    }
}
