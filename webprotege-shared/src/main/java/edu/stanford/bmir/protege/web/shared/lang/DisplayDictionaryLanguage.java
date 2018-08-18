package edu.stanford.bmir.protege.web.shared.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
@AutoValue
public abstract class DisplayDictionaryLanguage {

    @Nonnull
    public static DisplayDictionaryLanguage get(@Nonnull ImmutableList<DictionaryLanguage> primaryLanguages,
                                                @Nonnull ImmutableList<DictionaryLanguage> secondaryLanguages) {
        return new AutoValue_DisplayDictionaryLanguage(primaryLanguages,
                                                       secondaryLanguages);
    }

    @Nonnull
    public static DisplayDictionaryLanguage empty() {
        return get(ImmutableList.of(),
                   ImmutableList.of());
    }

    @Nonnull
    public abstract ImmutableList<DictionaryLanguage> getPrimaryLanguages();

    @Nonnull
    public abstract ImmutableList<DictionaryLanguage> getSecondaryLanguages();
}
