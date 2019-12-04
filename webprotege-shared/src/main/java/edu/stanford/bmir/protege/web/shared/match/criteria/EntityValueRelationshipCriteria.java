package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
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
@JsonTypeName("EntityValue")
public abstract class EntityValueRelationshipCriteria implements RelationshipValueCriteria {

    @Nonnull
    @JsonCreator
    public static EntityValueRelationshipCriteria get(@Nonnull EntityMatchCriteria criteria) {
        return new AutoValue_EntityValueRelationshipCriteria(criteria);
    }

    @Nonnull
    public abstract EntityMatchCriteria getEntityMatchCriteria();

    @Override
    public <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
