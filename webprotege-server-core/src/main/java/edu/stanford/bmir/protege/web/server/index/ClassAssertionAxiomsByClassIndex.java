package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
public interface ClassAssertionAxiomsByClassIndex {

    @Nonnull
    Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLClass cls,
                                                           @Nonnull OWLOntologyID ontologyId);
}
