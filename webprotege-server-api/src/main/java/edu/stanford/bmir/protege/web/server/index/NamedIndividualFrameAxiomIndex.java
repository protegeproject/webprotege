package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-06
 */
public interface NamedIndividualFrameAxiomIndex {

    /**
     * Gets the axioms that constitute the frame for a named individual
     * @param subject The subject of the frame
     * @return The set of axioms that make up the frame for the individual
     */
    @Nonnull
    Set<OWLAxiom> getNamedIndividualFrameAxioms(@Nonnull OWLNamedIndividual subject);
}
