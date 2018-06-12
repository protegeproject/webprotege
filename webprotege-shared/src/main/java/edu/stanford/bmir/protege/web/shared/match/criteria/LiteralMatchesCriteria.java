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
@JsonTypeName("LiteralMatches")
public abstract class LiteralMatchesCriteria implements LiteralCriteria {

    private static final String LEXICAL_VALUE = "lexicalValue";

    private static final String LANG_TAG = "langTag";

    private static final String DATATYPE = "datatype";

    @JsonProperty(LEXICAL_VALUE)
    @Nonnull
    public abstract LexicalValueCriteria getLexicalValueCriteria();

    @JsonProperty(LANG_TAG)
    @Nonnull
    public abstract LangTagCriteria getLangTagCriteria();

    @JsonProperty(DATATYPE)
    @Nonnull
    public abstract DatatypeCriteria getDatatypeCriteria();

    @Override
    public <R> R accept(@Nonnull LiteralCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @JsonCreator
    public static LiteralMatchesCriteria get(@Nonnull @JsonProperty(LEXICAL_VALUE) LexicalValueCriteria lexicalValueCritera,
                                             @Nonnull @JsonProperty(LANG_TAG) LangTagCriteria langTagCriteria,
                                             @Nonnull @JsonProperty(DATATYPE) DatatypeCriteria datatypeCriteria) {
        return new AutoValue_LiteralMatchesCriteria(lexicalValueCritera, langTagCriteria, datatypeCriteria);
    }

    public static LiteralMatchesCriteria lexicalValueMatches(@Nonnull LexicalValueCriteria criteria) {
        return get(criteria, LangTagMatchesCriteria.get(""), AnyDatatypeCriteria.get());
    }
}
