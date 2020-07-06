package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.DataPropertyRangeAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.Index;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
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
 * 2019-08-10
 */
public class DataPropertyRangeAxiomsIndexImpl implements DataPropertyRangeAxiomsIndex, DependentIndex {

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public DataPropertyRangeAxiomsIndexImpl(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLDataPropertyRangeAxiom> getDataPropertyRangeAxioms(@Nonnull OWLDataProperty dataProperty,
                                                                        @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(dataProperty);
        checkNotNull(ontologyID);
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.DATA_PROPERTY_RANGE, ontologyID)
                .filter(ax -> ax.getProperty().equals(dataProperty));
    }
}
