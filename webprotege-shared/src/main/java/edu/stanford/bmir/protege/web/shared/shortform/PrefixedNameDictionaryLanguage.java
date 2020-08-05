package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-05
 */
@AutoValue
@JsonTypeName(PrefixedNameDictionaryLanguage.TYPE_NAME)
public abstract class PrefixedNameDictionaryLanguage extends DictionaryLanguage {

    public static final String TYPE_NAME = "PrefixedName";

    @Override
    public <R> R accept(@Nonnull DictionaryLanguageVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
