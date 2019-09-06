package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByIndividualIndex;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
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
public class ClassAssertionAxiomsByIndividualIndexImpl implements ClassAssertionAxiomsByIndividualIndex {

    private final OntologyIndex ontologyIndex;

    @Inject
    public ClassAssertionAxiomsByIndividualIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Override
    public Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLIndividual individual,
                                                                  @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(individual);
        checkNotNull(ontologyID);
        return ontologyIndex.getOntology(ontologyID)
                .stream()
                .flatMap(ont -> ont.getClassAssertionAxioms(individual).stream());
    }
}
