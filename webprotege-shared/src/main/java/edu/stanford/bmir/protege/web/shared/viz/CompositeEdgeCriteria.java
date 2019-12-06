package edu.stanford.bmir.protege.web.shared.viz;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class CompositeEdgeCriteria implements EdgeCriteria {

    @Nonnull
    public static CompositeEdgeCriteria get(@Nonnull ImmutableList<EdgeCriteria> criteria) {
        return new AutoValue_CompositeEdgeCriteria(criteria);
    }

    @Override
    public <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    public abstract ImmutableList<EdgeCriteria> getCriteria();

}
