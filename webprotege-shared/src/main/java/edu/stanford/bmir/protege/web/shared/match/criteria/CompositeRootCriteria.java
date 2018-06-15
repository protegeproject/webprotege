package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class CompositeRootCriteria implements RootCriteria {

    @Nonnull
    public abstract ImmutableList<? extends RootCriteria> getRootCriteria();

    public static CompositeRootCriteria get(@Nonnull ImmutableList<? extends RootCriteria> rootCriteria) {
        return new AutoValue_CompositeRootCriteria(rootCriteria);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
