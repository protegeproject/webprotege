package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("StringContainsRegexMatch")
public abstract class StringContainsRegexMatchCriteria implements LexicalValueCriteria {

    @Nonnull
    public abstract String getPattern();

    public abstract boolean isIgnoreCase();

    @JsonCreator
    @Nonnull
    public static StringContainsRegexMatchCriteria get(@Nonnull String pattern, boolean ignoreCase) {
        return new AutoValue_StringContainsRegexMatchCriteria(pattern, ignoreCase);
    }

    @Override
    public <R> R accept(@Nonnull LexicalValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
