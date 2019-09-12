package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
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
@ProjectSingleton
public class ProjectAnnotationAssertionAxiomsBySubjectIndexImpl implements ProjectAnnotationAssertionAxiomsBySubjectIndex {

    @Nonnull
    private final ProjectOntologiesIndex ontologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Inject
    public ProjectAnnotationAssertionAxiomsBySubjectIndexImpl(@Nonnull ProjectOntologiesIndex ontologiesIndex,
                                                              @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex) {
        this.ontologiesIndex = checkNotNull(ontologiesIndex);
        this.annotationAssertionsIndex = checkNotNull(annotationAssertionsIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull OWLAnnotationSubject subject) {
        checkNotNull(subject);
        return ontologiesIndex.getOntologyIds()
                .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(subject, ontId));
    }
}
