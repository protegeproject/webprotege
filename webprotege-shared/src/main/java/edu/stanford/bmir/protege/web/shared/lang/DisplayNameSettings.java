package edu.stanford.bmir.protege.web.shared.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DisplayNameSettings {

    private static final String PRIMARY_DISPLAY_NAME_LANGUAGES = "primaryDisplayNameLanguages";

    private static final String SECONDARY_DISPLAY_NAME_LANGUAGES = "secondaryDisplayNameLanguages";

    @JsonCreator
    @Nonnull
    public static DisplayNameSettings get(@Nonnull @JsonProperty(PRIMARY_DISPLAY_NAME_LANGUAGES) ImmutableList<DictionaryLanguageData> primaryLanguages,
                                          @Nonnull @JsonProperty(SECONDARY_DISPLAY_NAME_LANGUAGES) ImmutableList<DictionaryLanguageData> secondaryLanguages) {
        return new AutoValue_DisplayNameSettings(primaryLanguages,
                                                 secondaryLanguages);
    }

    @Nonnull
    public static DisplayNameSettings empty() {
        return get(ImmutableList.of(),
                   ImmutableList.of());
    }

    @JsonProperty(PRIMARY_DISPLAY_NAME_LANGUAGES)
    @Nonnull
    public abstract ImmutableList<DictionaryLanguageData> getPrimaryDisplayNameLanguages();

    @JsonProperty(SECONDARY_DISPLAY_NAME_LANGUAGES)
    @Nonnull
    public abstract ImmutableList<DictionaryLanguageData> getSecondaryDisplayNameLanguages();
}
