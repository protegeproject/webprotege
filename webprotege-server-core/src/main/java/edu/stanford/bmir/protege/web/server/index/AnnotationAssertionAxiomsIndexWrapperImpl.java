package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class AnnotationAssertionAxiomsIndexWrapperImpl implements AnnotationAssertionAxiomsIndex {

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public AnnotationAssertionAxiomsIndexWrapperImpl(@Nonnull OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms() {
        return this.rootOntology
                .getImportsClosure()
                .stream()
                .flatMap(ont -> ont.getAxioms(AxiomType.ANNOTATION_ASSERTION).stream());
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull IRI subject) {
        return rootOntology.getAnnotationAssertionAxioms(subject).stream();
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull IRI subject, @Nonnull OWLAnnotationProperty property) {
        return rootOntology.getAnnotationAssertionAxioms(subject).stream()
                           .filter(ax -> ax.getProperty().equals(property));
    }

    @Override
    public long getAnnotationAssertionAxiomsCount(@Nonnull IRI subject) {
        return getAnnotationAssertionAxioms(subject).count();
    }

    @Override
    public long getAnnotationAssertionAxiomsCount(@Nonnull IRI subject, @Nonnull OWLAnnotationProperty property) {
        return getAnnotationAssertionAxioms(subject, property).count();
    }
}
