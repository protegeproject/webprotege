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
@JsonTypeName("AnyValue")
public abstract class AnyRelationshipValueCriteria implements RelationshipValueCriteria {

    @Nonnull
    @JsonCreator
    public static AnyRelationshipValueCriteria get() {
        return new AutoValue_AnyRelationshipValueCriteria();
    }

    @Override
    public <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
