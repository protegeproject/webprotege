package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.index.DeprecatedEntitiesByEntityIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

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
    private final DeprecatedEntitiesByEntityIndex deprecatedEntitiesByEntityIndex;

    @Inject
    public DeprecatedEntityCheckerImpl(@Nonnull DeprecatedEntitiesByEntityIndex deprecatedEntitiesByEntityIndex) {
        this.deprecatedEntitiesByEntityIndex = checkNotNull(deprecatedEntitiesByEntityIndex);
    }

    @Override
    public boolean isDeprecated(OWLEntity entity) {
        return deprecatedEntitiesByEntityIndex.isDeprecated(entity);
    }
}
