package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@ProjectSingleton
public interface ProjectAnnotationAssertionAxiomsBySubjectIndex {

    /**
     * Gets the {@link OWLAnnotationAssertionAxiom}s that are contained in project
     * ontologies and have the specified subject.
     * @param subject The subject
     */
    @Nonnull
    Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull OWLAnnotationSubject subject);
}
