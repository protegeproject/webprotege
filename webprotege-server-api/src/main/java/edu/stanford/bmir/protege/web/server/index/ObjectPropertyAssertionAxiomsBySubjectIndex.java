package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-11
 */
@ProjectSingleton
public interface ObjectPropertyAssertionAxiomsBySubjectIndex extends Index {

    /**
     * Gets the {@link OWLObjectPropertyAssertionAxiom}s that have the specified individual as
     * a subject.
     * @param subject The subject.
     * @param ontologyId The id of the ontology to examine.
     */
    @Nonnull
    Stream<OWLObjectPropertyAssertionAxiom> getObjectPropertyAssertions(@Nonnull
                                                                        OWLIndividual subject,
                                                                        @Nonnull
                                                                        OWLOntologyID ontologyId);
}
