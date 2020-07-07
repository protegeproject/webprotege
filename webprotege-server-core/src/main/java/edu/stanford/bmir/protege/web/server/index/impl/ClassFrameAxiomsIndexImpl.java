package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.server.index.ClassFrameAxiomsIndex.AnnotationsTreatment.INCLUDE_ANNOTATIONS;

@ProjectSingleton
public class ClassFrameAxiomsIndexImpl implements ClassFrameAxiomsIndex {

    @Nonnull
    private final ProjectOntologiesIndex ontologiesIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsIndex;

    @Inject
    public ClassFrameAxiomsIndexImpl(@Nonnull ProjectOntologiesIndex ontologiesIndex,
                                     @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex,
                                     @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                                     @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsIndex) {
        this.ontologiesIndex = checkNotNull(ontologiesIndex);
        this.subClassOfAxiomsIndex = checkNotNull(subClassOfAxiomsIndex);
        this.equivalentClassesAxiomsIndex = checkNotNull(equivalentClassesAxiomsIndex);
        this.annotationAssertionAxiomsIndex = checkNotNull(annotationAssertionAxiomsIndex);
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getFrameAxioms(@Nonnull OWLClass subject, @Nonnull AnnotationsTreatment annotationsTreatment) {
        var subClassOfAxioms = getFrameSubClassOfAxioms(subject);
        var equivalentClassesAxioms = getFrameEquivalentClassesAxioms(subject);
        var annotationAssertions = Stream.<OWLAnnotationAssertionAxiom>empty();
        if (annotationsTreatment == INCLUDE_ANNOTATIONS) {
            annotationAssertions = getFrameAnnotationAssertionsAxiom(subject);
        }
        return Stream.of(subClassOfAxioms,
            equivalentClassesAxioms,
            annotationAssertions)
            .flatMap(ax -> ax)
            .collect(toImmutableSet());
    }

    private Stream<OWLSubClassOfAxiom> getFrameSubClassOfAxioms(OWLClass subject) {
        return ontologiesIndex
            .getOntologyIds()
            .flatMap(ontId -> subClassOfAxiomsIndex.getSubClassOfAxiomsForSubClass(subject, ontId));
    }

    private Stream<OWLEquivalentClassesAxiom> getFrameEquivalentClassesAxioms(OWLClass subject) {
        return ontologiesIndex.getOntologyIds()
            .flatMap(ontId -> equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(subject,
                ontId));
    }

    private Stream<OWLAnnotationAssertionAxiom> getFrameAnnotationAssertionsAxiom(OWLClass subject) {
        return ontologiesIndex.getOntologyIds()
            .flatMap(ontId -> annotationAssertionAxiomsIndex.getAxiomsForSubject(subject.getIRI(),
                ontId));
    }
}
