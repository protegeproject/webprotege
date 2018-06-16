package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;
import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("DateIsBefore")
public abstract class DateIsBeforeCriteria implements LexicalValueCriteria {

    @JsonProperty("year")
    public abstract int getYear();

    @JsonProperty("month")
    public abstract int getMonth();

    @JsonProperty("day")
    public abstract int getDay();

    public static DateIsBeforeCriteria get(@JsonProperty("year") int year,
                                           @JsonProperty("month") int month,
                                           @JsonProperty("day") int day) {
        checkArgument(month >= 1 && month <= 12);
        checkArgument(day >= 1 && day <= 31);
        return new AutoValue_DateIsBeforeCriteria(year,
                                                  month,
                                                  day);
    }

    @Override
    public <R> R accept(@Nonnull LexicalValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
