package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.DataPropertyAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
public class DataPropertyAssertionAxiomsBySubjectIndexImpl implements DataPropertyAssertionAxiomsBySubjectIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public DataPropertyAssertionAxiomsBySubjectIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLDataPropertyAssertionAxiom> getDataPropertyAssertions(@Nonnull OWLIndividual individual,
                                                                           @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(individual);
        checkNotNull(ontologyId);
        return ontologyIndex.getOntology(ontologyId)
                .stream()
                .flatMap(ont -> ont.getDataPropertyAssertionAxioms(individual).stream());
    }
}
