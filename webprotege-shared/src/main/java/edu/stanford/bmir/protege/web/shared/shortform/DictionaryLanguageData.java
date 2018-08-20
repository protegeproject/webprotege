package edu.stanford.bmir.protege.web.shared.shortform;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DictionaryLanguageData {

    @Nonnull
    public static DictionaryLanguageData get(@Nonnull OWLAnnotationPropertyData propertyData,
                                             @Nonnull String lang) {
        return new AutoValue_DictionaryLanguageData(propertyData, lang);
    }

    @Nonnull
    public static DictionaryLanguageData getRdfsLabelWithEmptyLang() {
        return getRdfsLabelWithLang("");
    }

    @Nonnull
    public static DictionaryLanguageData getRdfsLabelWithLang(@Nonnull String lang) {
        return get(DataFactory.getRdfsLabelData(), lang);
    }

    @Nonnull
    public Optional<OWLAnnotationPropertyData> getAnnotationPropertyData() {
        return Optional.ofNullable(getPropertyData());
    }

    @Nonnull
    protected abstract OWLAnnotationPropertyData getPropertyData();


    @Nonnull
    public abstract String getLanguageTag();


    public DictionaryLanguage toDictionaryLanguage() {
        return DictionaryLanguage.create(getPropertyData().getEntity().getIRI(),
                                         getLanguageTag());
    }
}
