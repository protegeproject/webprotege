package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@GwtCompatible(serializable = true)
@JsonTypeName("LangTagMatches")
public abstract class LangTagMatchesCriteria implements LangTagCriteria {

    private static final String RANGE = "languageRange";

    @JsonProperty(RANGE)
    public abstract String getLanguageRange();

    @JsonCreator
    @Nonnull
    public static LangTagMatchesCriteria get(@Nonnull @JsonProperty(RANGE) String languageRange) {
        return new AutoValue_LangTagMatchesCriteria(languageRange);
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
