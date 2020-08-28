package edu.stanford.bmir.protege.web.shared.lang;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DictionaryLanguageUsage {

    public static DictionaryLanguageUsage get(@Nonnull DictionaryLanguage language,
                                              int referenceCount) {
        return new AutoValue_DictionaryLanguageUsage(language, referenceCount);
    }

    @Nonnull
    public abstract DictionaryLanguage getDictionaryLanguage();

    public abstract int getReferenceCount();
}
