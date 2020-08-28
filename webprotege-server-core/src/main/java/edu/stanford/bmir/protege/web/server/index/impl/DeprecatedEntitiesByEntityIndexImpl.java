package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.shortform.LuceneIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public class DeprecatedEntitiesByEntityIndexImpl implements DeprecatedEntitiesByEntityIndex, DependentIndex {

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


    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex, annotationAssertionsIndex);
    }

    @Override
    public boolean isDeprecated(@Nonnull OWLEntity entity) {
        return projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(entity.getIRI(), ontId))
                .anyMatch(OWLAnnotationAssertionAxiom::isDeprecatedIRIAssertion);
    }
}
