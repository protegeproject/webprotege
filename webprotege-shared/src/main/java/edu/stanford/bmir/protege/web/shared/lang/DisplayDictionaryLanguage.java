package edu.stanford.bmir.protege.web.shared.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
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
    public static DisplayDictionaryLanguage get(@Nonnull Optional<DictionaryLanguage> primaryLanguage,
                                                @Nonnull Optional<DictionaryLanguage> secondaryLanguage) {
        return new AutoValue_DisplayDictionaryLanguage(primaryLanguage,
                                                          secondaryLanguage);
    }

    @Nonnull
    public static DisplayDictionaryLanguage empty() {
        return get(Optional.empty(),
                   Optional.empty());
    }

    @Nonnull
    public abstract Optional<DictionaryLanguage> getPrimaryLanguage();

    @Nonnull
    public abstract Optional<DictionaryLanguage> getSecondaryLanguage();
}
