package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

import java.time.Year;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("DateIsAfter")
public abstract class DateIsAfterCriteria implements DateCriteria {

    @Nonnull
    @JsonCreator
    public static DateIsAfterCriteria get(@JsonProperty(YEAR) int year,
                                           @JsonProperty(MONTH) int month,
                                           @JsonProperty(DAY) int day) {
        DateCriteria.checkArgs(year, month, day);
        return new AutoValue_DateIsAfterCriteria(year,
                                                  month,
                                                  day);
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
