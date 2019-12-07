package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import jsinterop.annotations.JsProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CompositeOf")
public abstract class CompositeEdgeCriteria implements EdgeCriteria {

    @JsonCreator
    @Nonnull
    public static CompositeEdgeCriteria get(@Nonnull @JsonProperty("criteria") ImmutableList<EdgeCriteria> criteria,
                                            @Nonnull @JsonProperty("matchType") MultiMatchType multiMatchType) {
        return new AutoValue_CompositeEdgeCriteria(criteria, multiMatchType);
    }

    @Override
    public <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    public abstract ImmutableList<EdgeCriteria> getCriteria();

    @Nonnull
    public abstract MultiMatchType getMatchType();
}
