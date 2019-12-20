package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-19
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("NoEdge")
public abstract class NoEdgeCriteria implements EdgeCriteria {

    @JsonCreator
    public static NoEdgeCriteria get() {
        return new AutoValue_NoEdgeCriteria();
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
