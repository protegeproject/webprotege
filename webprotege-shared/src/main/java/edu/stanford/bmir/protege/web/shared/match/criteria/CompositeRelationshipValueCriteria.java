package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-23
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CompositeCriteria")
public abstract class CompositeRelationshipValueCriteria implements RelationshipValueCriteria {

    @JsonCreator
    public static CompositeRelationshipValueCriteria get(@JsonProperty("multiMatchType") @Nonnull MultiMatchType multiMatchType,
                                                         @JsonProperty("criteria") @Nonnull ImmutableList<RelationshipValueCriteria> criteria) {
        return new AutoValue_CompositeRelationshipValueCriteria(multiMatchType, criteria);
    }

    @Nonnull
    public abstract MultiMatchType getMultiMatchType();

    @Nonnull
    public abstract ImmutableList<RelationshipValueCriteria> getCriteria();

    @Override
    public <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
