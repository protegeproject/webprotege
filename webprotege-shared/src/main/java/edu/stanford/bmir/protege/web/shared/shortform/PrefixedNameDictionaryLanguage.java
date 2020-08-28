package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-05
 */
@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName(PrefixedNameDictionaryLanguage.TYPE_NAME)
public abstract class PrefixedNameDictionaryLanguage extends DictionaryLanguage {

    public static final String TYPE_NAME = "PrefixedName";

    @JsonCreator
    @Nonnull
    public static PrefixedNameDictionaryLanguage get() {
        return new AutoValue_PrefixedNameDictionaryLanguage();
    }

    @Override
    public boolean isAnnotationBased() {
        return false;
    }

    @Override
    public <R> R accept(@Nonnull DictionaryLanguageVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public String getLang() {
        return "";
    }
}
