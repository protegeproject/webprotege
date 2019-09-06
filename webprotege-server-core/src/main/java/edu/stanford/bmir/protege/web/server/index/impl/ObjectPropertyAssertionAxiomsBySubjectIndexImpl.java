package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.ObjectPropertyAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
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
public class ObjectPropertyAssertionAxiomsBySubjectIndexImpl implements ObjectPropertyAssertionAxiomsBySubjectIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public ObjectPropertyAssertionAxiomsBySubjectIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLObjectPropertyAssertionAxiom> getObjectPropertyAssertions(@Nonnull OWLIndividual subject,
                                                                               @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subject);
        checkNotNull(ontologyId);
        return ontologyIndex.getOntology(ontologyId)
                .stream()
                .flatMap(ont -> ont.getObjectPropertyAssertionAxioms(subject).stream());
    }
}
