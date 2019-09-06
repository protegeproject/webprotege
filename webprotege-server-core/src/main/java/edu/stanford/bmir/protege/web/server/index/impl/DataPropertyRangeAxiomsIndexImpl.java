package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.DataPropertyRangeAxiomsIndex;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public class DataPropertyRangeAxiomsIndexImpl implements DataPropertyRangeAxiomsIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public DataPropertyRangeAxiomsIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLDataPropertyRangeAxiom> getDataPropertyRangeAxioms(@Nonnull OWLDataProperty dataProperty,
                                                                        @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(dataProperty);
        checkNotNull(ontologyID);
        return ontologyIndex.getOntology(ontologyID)
                .stream()
                .flatMap(ont -> ont.getDataPropertyRangeAxioms(dataProperty).stream());
    }
}
