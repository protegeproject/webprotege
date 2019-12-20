package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("NegationOf")
public abstract class NegatedEdgeCriteria implements EdgeCriteria {

    @JsonCreator
    public static NegatedEdgeCriteria get(@Nonnull @JsonProperty("negatedCriteria") EdgeCriteria negatedCriteria) {
        return new AutoValue_NegatedEdgeCriteria(negatedCriteria);
    }

    @Nonnull
    public abstract EdgeCriteria getNegatedCriteria();

    @Override
    public <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public EdgeCriteria simplify() {
        EdgeCriteria negatedCriteria = getNegatedCriteria().simplify();
        if(negatedCriteria instanceof NoEdgeCriteria) {
            // Remove double negative
            return AnyEdgeCriteria.get();
        }
        else {
            return NegatedEdgeCriteria.get(negatedCriteria);
        }
    }
}
