package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-18
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class CompositeLiteralCriteria implements LiteralCriteria {

    public static CompositeLiteralCriteria get(@Nonnull ImmutableList<? extends LiteralCriteria> criteria,
                                               @Nonnull MultiMatchType matchType) {
        return new AutoValue_CompositeLiteralCriteria(ImmutableList.copyOf(criteria), matchType);
    }

    @Nonnull
    public abstract ImmutableList<LiteralCriteria> getCriteria();

    @Nonnull
    public abstract MultiMatchType getMultiMatchType();

    @Override
    public <R> R accept(@Nonnull LiteralCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
