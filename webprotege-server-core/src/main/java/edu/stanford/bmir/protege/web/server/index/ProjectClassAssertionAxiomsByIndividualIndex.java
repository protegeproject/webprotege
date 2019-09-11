package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.server.index.impl.Index;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public interface ProjectClassAssertionAxiomsByIndividualIndex extends Index {

    @Nonnull
    Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull
                                                           OWLIndividual individual);
}
