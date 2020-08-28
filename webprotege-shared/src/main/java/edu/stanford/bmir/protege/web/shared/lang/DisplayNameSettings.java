package edu.stanford.bmir.protege.web.shared.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

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
    public static DisplayNameSettings get(@Nullable @JsonProperty(PRIMARY_DISPLAY_NAME_LANGUAGES) ImmutableList<DictionaryLanguage> primaryLanguages,
                                          @Nullable @JsonProperty(SECONDARY_DISPLAY_NAME_LANGUAGES) ImmutableList<DictionaryLanguage> secondaryLanguages) {
        return new AutoValue_DisplayNameSettings(primaryLanguages == null ? ImmutableList.of() : primaryLanguages,
                                                 secondaryLanguages == null ? ImmutableList.of() : secondaryLanguages);
    }

    @Nonnull
    public static DisplayNameSettings empty() {
        return get(ImmutableList.of(),
                   ImmutableList.of());
    }

    @JsonProperty(PRIMARY_DISPLAY_NAME_LANGUAGES)
    @Nonnull
    public abstract ImmutableList<DictionaryLanguage> getPrimaryDisplayNameLanguages();

    @JsonProperty(SECONDARY_DISPLAY_NAME_LANGUAGES)
    @Nonnull
    public abstract ImmutableList<DictionaryLanguage> getSecondaryDisplayNameLanguages();

    public boolean hasDisplayNameLanguageForLangTag(@Nonnull String langTag) {
        Stream<DictionaryLanguage> languages = Streams.concat(
                getPrimaryDisplayNameLanguages().stream(),
                getSecondaryDisplayNameLanguages().stream()
        );
        return languages.anyMatch(l -> langTag.equalsIgnoreCase(l.getLang()));

    }
}
