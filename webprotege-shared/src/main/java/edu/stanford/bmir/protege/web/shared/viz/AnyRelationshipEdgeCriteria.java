package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
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
@JsonTypeName("AnyRelationshipEdge")
public class AnyRelationshipEdgeCriteria implements EdgeTypeCriteria {

    @JsonCreator
    @Nonnull
    public static AnyRelationshipEdgeCriteria get() {
        return new AutoValue_AnyRelationshipEdgeCriteria();
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
