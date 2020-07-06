package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
@ProjectSingleton
public interface EntitiesInProjectSignatureIndex extends Index {

    boolean containsEntityInSignature(@Nonnull OWLEntity entity);
}
