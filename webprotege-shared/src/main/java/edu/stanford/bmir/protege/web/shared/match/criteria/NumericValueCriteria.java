package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@AutoValue
@JsonTypeName("LexicalNumericValue")
@GwtCompatible(serializable = true)
@JsonPropertyOrder({NumericValueCriteria.PREDICATE, NumericValueCriteria.VALUE})
public abstract class NumericValueCriteria implements LexicalValueCriteria {

    public static final String PREDICATE = "predicate";

    public static final String VALUE = "value";

    @Nonnull
    @JsonProperty(PREDICATE)
    public abstract NumericPredicate getPredicate();

    public abstract double getValue();


    @JsonCreator
    @Nonnull
    public static NumericValueCriteria get(@Nonnull @JsonProperty(PREDICATE) NumericPredicate predicate,
                                           @JsonProperty(VALUE) double value) {
        return new AutoValue_NumericValueCriteria(predicate, value);
    }

    @Nonnull
    public static NumericValueCriteria numericValue(@Nonnull NumericPredicate predicate, double value) {
        return get(predicate, value);
    }

    @Override
    public <R> R accept(@Nonnull LexicalValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
