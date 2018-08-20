package edu.stanford.bmir.protege.web.shared.lang;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
@AutoValue
public abstract class DisplayNameSettings {

    @Nonnull
    public static DisplayNameSettings get(@Nonnull ImmutableList<DictionaryLanguage> primaryLanguages,
                                          @Nonnull ImmutableList<DictionaryLanguage> secondaryLanguages) {
        return new AutoValue_DisplayNameSettings(primaryLanguages,
                                                 secondaryLanguages);
    }

    @Nonnull
    public static DisplayNameSettings empty() {
        return get(ImmutableList.of(),
                   ImmutableList.of());
    }

    @Nonnull
    public abstract ImmutableList<DictionaryLanguage> getPrimaryDisplayNameLanguages();

    @Nonnull
    public abstract ImmutableList<DictionaryLanguage> getSecondaryDisplayNameLanguages();
}
