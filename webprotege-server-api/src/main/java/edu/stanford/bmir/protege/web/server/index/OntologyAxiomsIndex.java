package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLAxiom;

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
                          @Nonnull OntologyDocumentId ontologyDocumentId);

    boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom,
                                           @Nonnull OntologyDocumentId ontologyDocumentId);

    @Nonnull
    Stream<OWLAxiom> getAxioms(@Nonnull OntologyDocumentId ontologyDocumentId);
}
