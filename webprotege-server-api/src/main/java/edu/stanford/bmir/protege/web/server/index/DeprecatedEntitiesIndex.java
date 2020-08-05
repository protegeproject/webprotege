package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-04
 */
@ProjectSingleton
public interface DeprecatedEntitiesIndex {

    @Nonnull
    Page<OWLEntity> getDeprecatedEntities(@Nonnull Set<EntityType<?>> entityTypes,
                                          @Nonnull PageRequest pageRequest);
}
