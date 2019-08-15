package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
public interface ProjectAnnotationAssertionAxiomsBySubjectIndex {

    @Nonnull
    Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull OWLAnnotationSubject subject);
}
