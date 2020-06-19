package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("StringContains")
public abstract class StringContainsCriteria implements SimpleStringCriteria {

    @JsonCreator
    @Nonnull
    public static StringContainsCriteria get(@Nullable @JsonProperty(VALUE) String value,
                                             @JsonProperty(IGNORE_CASE) boolean ignoreCase) {
        return new AutoValue_StringContainsCriteria(value == null ? "" : value, ignoreCase);
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
