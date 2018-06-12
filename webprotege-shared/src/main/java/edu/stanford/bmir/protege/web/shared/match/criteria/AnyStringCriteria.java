package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
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
@JsonTypeName("AnyString")
public abstract class AnyStringCriteria implements LexicalValueCriteria {

    @JsonCreator
    @Nonnull
    public static AnyStringCriteria get() {
        return new AutoValue_AnyStringCriteria();
    }

    @Override
    public <R> R accept(@Nonnull LexicalValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
