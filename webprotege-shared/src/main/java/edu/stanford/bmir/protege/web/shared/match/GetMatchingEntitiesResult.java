package edu.stanford.bmir.protege.web.shared.match;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetMatchingEntitiesResult implements Result {

    @Nonnull
    public abstract Page<EntityNode> getEntities();

    @Nonnull
    public static GetMatchingEntitiesResult get(@Nonnull Page<EntityNode> entities) {
        return new AutoValue_GetMatchingEntitiesResult(entities);
    }
}
