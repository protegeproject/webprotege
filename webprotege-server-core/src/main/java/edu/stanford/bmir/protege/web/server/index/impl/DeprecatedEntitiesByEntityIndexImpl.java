package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.DeprecatedEntitiesByEntityIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public class DeprecatedEntitiesByEntityIndexImpl implements DeprecatedEntitiesByEntityIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Inject
    public DeprecatedEntitiesByEntityIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                               @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.annotationAssertionsIndex = checkNotNull(annotationAssertionsIndex);
    }

    @Override
    public boolean isDeprecated(@Nonnull OWLEntity entity) {
        return projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(entity.getIRI(), ontId))
                .anyMatch(OWLAnnotationAssertionAxiom::isDeprecatedIRIAssertion);
    }
}
