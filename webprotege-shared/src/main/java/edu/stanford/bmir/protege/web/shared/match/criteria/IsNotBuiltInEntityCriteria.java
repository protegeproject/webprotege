package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("IsNotBuiltInEntity")
public abstract class IsNotBuiltInEntityCriteria implements EntityMatchCriteria {

    @JsonCreator
    @Nonnull
    public static IsNotBuiltInEntityCriteria get() {
        return new AutoValue_IsNotBuiltInEntityCriteria();
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
