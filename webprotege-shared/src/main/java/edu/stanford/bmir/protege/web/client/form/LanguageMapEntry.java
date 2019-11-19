package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
@AutoValue
public abstract class LanguageMapEntry {

    public static LanguageMapEntry get(@Nonnull String langTag,
                                       @Nonnull String value) {
        return new AutoValue_LanguageMapEntry(langTag, value);
    }

    @Nonnull
    public abstract String getLangTag();

    @Nonnull
    public abstract String getValue();
}
