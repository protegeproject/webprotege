package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.HasAnnotationAssertionAxioms;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

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
    public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull OWLAnnotationSubject subject) {
        checkNotNull(subject);
        return ontologiesIndex.getOntologyIds()
                .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(subject, ontId));
    }
}
