package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.DeprecatedEntitiesIndex;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@ProjectSingleton
public class DeprecatedEntityCheckerImpl implements DeprecatedEntityChecker {

    @Nonnull
    private final DeprecatedEntitiesIndex deprecatedEntitiesIndex;

    @Inject
    public DeprecatedEntityCheckerImpl(@Nonnull DeprecatedEntitiesIndex deprecatedEntitiesIndex) {
        this.deprecatedEntitiesIndex = checkNotNull(deprecatedEntitiesIndex);
    }

    @Override
    public boolean isDeprecated(OWLEntity entity) {
        return deprecatedEntitiesIndex.isDeprecated(entity);
    }
}
