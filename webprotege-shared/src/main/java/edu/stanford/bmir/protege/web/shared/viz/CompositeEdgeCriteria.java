package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import jsinterop.annotations.JsProperty;

import javax.annotation.Nonnull;
import java.util.List;

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
    public static CompositeEdgeCriteria get(@Nonnull @JsonProperty("criteria") List<? extends EdgeCriteria> criteria,
                                            @Nonnull @JsonProperty("matchType") MultiMatchType multiMatchType) {
        return new AutoValue_CompositeEdgeCriteria(ImmutableList.copyOf(criteria), multiMatchType);
    }

    @Nonnull
    public static CompositeEdgeCriteria get(@Nonnull MultiMatchType multiMatchType,
                                            @Nonnull EdgeCriteria ... criteria) {
        return get(ImmutableList.copyOf(criteria), multiMatchType);
    }

    @Nonnull
    public static CompositeEdgeCriteria empty() {
        return get(ImmutableList.of(), MultiMatchType.ANY);
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
