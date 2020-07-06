package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public interface DeprecatedEntitiesByEntityIndex extends Index {

    boolean isDeprecated(@Nonnull OWLEntity entity);
}
