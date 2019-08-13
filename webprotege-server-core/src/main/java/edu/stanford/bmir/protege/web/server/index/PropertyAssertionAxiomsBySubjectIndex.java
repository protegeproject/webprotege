package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
public interface PropertyAssertionAxiomsBySubjectIndex {

    @Nonnull
    Stream<OWLAxiom> getPropertyAssertions(@Nonnull
                                                                                                                                   OWLIndividual individual,
                                                                                                                                   @Nonnull
                                                                                                                                   OWLOntologyID ontologyId);
}
