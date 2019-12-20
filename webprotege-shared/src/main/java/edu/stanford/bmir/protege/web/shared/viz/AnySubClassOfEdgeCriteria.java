package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("AnySubClassOfEdge")
public abstract class AnySubClassOfEdgeCriteria implements EdgeTypeCriteria {

    @JsonCreator
    @Nonnull
    public static AnySubClassOfEdgeCriteria get() {
        return new AutoValue_AnySubClassOfEdgeCriteria();
    }

    @Override
    public <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public EdgeCriteria simplify() {
        return this;
    }
}
