package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ValueMatches")
public abstract class RelationshipValueMatchesCriteria implements RelationshipValueCriteria {

    @Nonnull
    @JsonCreator
    public static RelationshipValueMatchesCriteria get(@Nonnull @JsonProperty("matchCriteria") EntityMatchCriteria criteria) {
        return new AutoValue_RelationshipValueMatchesCriteria(criteria);
    }

    @Nonnull
    public abstract EntityMatchCriteria getMatchCriteria();

    @Override
    public <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
