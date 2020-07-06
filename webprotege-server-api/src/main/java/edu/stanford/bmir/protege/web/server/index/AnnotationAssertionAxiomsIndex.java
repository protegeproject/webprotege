package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
@ProjectSingleton
public interface AnnotationAssertionAxiomsIndex extends Index {

    Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms();

    Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull IRI subject);

    Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull IRI subject,
                                                                     @Nonnull OWLAnnotationProperty property);

    long getAnnotationAssertionAxiomsCount(@Nonnull IRI subject);

    long getAnnotationAssertionAxiomsCount(@Nonnull IRI subject,
                                           @Nonnull OWLAnnotationProperty property);
}
