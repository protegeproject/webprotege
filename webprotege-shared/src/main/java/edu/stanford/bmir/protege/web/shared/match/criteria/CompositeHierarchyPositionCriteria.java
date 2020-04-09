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
 * 2020-04-08
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CompositeCriteria")
public abstract class CompositeHierarchyPositionCriteria implements HierarchyPositionCriteria {

    public static final String MATCH_TYPE = "matchType";

    public static final String CRITERIA = "criteria";

    @JsonCreator
    public static CompositeHierarchyPositionCriteria get(@JsonProperty(CRITERIA) @Nonnull ImmutableList<? extends HierarchyPositionCriteria> criteria,
                                                         @JsonProperty(MATCH_TYPE) @Nonnull MultiMatchType multiMatchType) {
        return new AutoValue_CompositeHierarchyPositionCriteria(multiMatchType, ImmutableList.copyOf(criteria));
    }

    public static CompositeHierarchyPositionCriteria get() {
        return get(ImmutableList.of(),
                   MultiMatchType.ALL);
    }

    @JsonProperty(MATCH_TYPE)
    @Nonnull
    public abstract MultiMatchType getMatchType();

    @JsonProperty(CRITERIA)
    public abstract ImmutableList<HierarchyPositionCriteria> getCriteria();

    @Override
    public <R> R accept(@Nonnull HierarchyPositionCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
