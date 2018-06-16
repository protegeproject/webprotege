package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import jsinterop.annotations.JsIgnore;

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

    @JsonIgnore
    protected abstract int getMultiMatchTypeOrdinal();

    @Nonnull
    public MultiMatchType getMatchType() {
        return MultiMatchType.values()[getMultiMatchTypeOrdinal()];
    }


    public static CompositeRootCriteria get(@Nonnull ImmutableList<? extends RootCriteria> rootCriteria,
                                            @Nonnull MultiMatchType matchType) {
        return new AutoValue_CompositeRootCriteria(rootCriteria, matchType.ordinal());
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
