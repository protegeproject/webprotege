package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@ProjectSingleton
public interface OntologyAxiomsIndex extends Index {

    boolean containsAxiom(@Nonnull OWLAxiom axiom,
                          @Nonnull OWLOntologyID ontologyId);

    boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom,
                                           @Nonnull OWLOntologyID ontologyId);

    @Nonnull
    Stream<OWLAxiom> getAxioms(@Nonnull OWLOntologyID ontologyId);
}
