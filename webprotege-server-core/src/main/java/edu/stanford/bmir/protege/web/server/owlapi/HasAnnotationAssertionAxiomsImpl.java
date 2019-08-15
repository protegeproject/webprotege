package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.HasAnnotationAssertionAxioms;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
public class HasAnnotationAssertionAxiomsImpl implements HasAnnotationAssertionAxioms {

    @Nonnull
    private final ProjectOntologiesIndex ontologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Inject
    public HasAnnotationAssertionAxiomsImpl(@Nonnull ProjectOntologiesIndex ontologiesIndex,
                                            @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex) {
        this.ontologiesIndex = checkNotNull(ontologiesIndex);
        this.annotationAssertionsIndex = checkNotNull(annotationAssertionsIndex);
    }

    @Override
    public Set<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull OWLAnnotationSubject subject) {
        checkNotNull(subject);
        return ontologiesIndex.getOntologyIds()
                .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(subject, ontId))
                .collect(toImmutableSet());
    }
}
