package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@AutoValue
@JsonTypeName("NumericValue")
@GwtCompatible(serializable = true)
@JsonPropertyOrder({NumericValueCriteria.PREDICATE, NumericValueCriteria.VALUE})
public abstract class NumericValueCriteria implements LexicalValueCriteria {

    public static final String PREDICATE = "predicate";

    public static final String VALUE = "value";

    @Nonnull
    @JsonProperty(PREDICATE)
    public NumericPredicate getPredicate() {
        return NumericPredicate.valueOf(getName());
    }

    @JsonIgnore
    protected abstract String getName();

    // This weirdness is due to a problem I'm having with GWT serializing embedded enums
    // if the enum isn't reachable in a way other than through a custom field serializer
    public abstract double getValue();



    @Nonnull
    public static NumericValueCriteria get(@Nonnull String name, @JsonProperty(VALUE) double value) {
        return new AutoValue_NumericValueCriteria(name, value);
    }

    @JsonCreator
    @Nonnull
    public static NumericValueCriteria get(@Nonnull @JsonProperty(PREDICATE) NumericPredicate predicate,
                                           @JsonProperty(VALUE) double value) {
        return new AutoValue_NumericValueCriteria(predicate.name(), value);
    }

    @Nonnull
    public static NumericValueCriteria numericValue(@Nonnull NumericPredicate predicate, double value) {
        return get(predicate, value);
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull LiteralCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
