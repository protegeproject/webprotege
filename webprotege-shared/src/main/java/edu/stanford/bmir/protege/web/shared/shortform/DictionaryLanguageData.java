package edu.stanford.bmir.protege.web.shared.shortform;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DictionaryLanguageData {

    public static DictionaryLanguageData get(@Nonnull OWLAnnotationPropertyData propertyData,
                                      @Nonnull String lang) {
        return new AutoValue_DictionaryLanguageData(propertyData, lang);
    }

    public static DictionaryLanguageData get(@Nonnull String lang) {
        return new AutoValue_DictionaryLanguageData(null, lang);
    }

    @Nonnull
    public Optional<OWLAnnotationPropertyData> getAnnotationPropertyData() {
        return Optional.ofNullable(getPropertyData());
    }

    @Nullable
    protected abstract OWLAnnotationPropertyData getPropertyData();


    @Nonnull
    public abstract String getLanguage();
}
