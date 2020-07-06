package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.DataPropertyCharacteristicsIndex;
import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.Index;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public class DataPropertyCharacteristicsIndexImpl implements DataPropertyCharacteristicsIndex, DependentIndex {

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public DataPropertyCharacteristicsIndexImpl(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByTypeIndex);
    }

    @Override
    public boolean isFunctional(@Nonnull OWLDataProperty dataProperty,
                                @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(dataProperty);
        checkNotNull(ontologyId);
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.FUNCTIONAL_DATA_PROPERTY, ontologyId)
                .anyMatch(axiom -> axiom.getProperty().equals(dataProperty));
    }
}
