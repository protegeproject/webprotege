package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Optional;

import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DictionaryLanguageData {

    private static final String PROPERTY_IRI = "propertyIri";

    private static final String LANGUAGE_TAG = "lang";

    private DictionaryLanguage dictionaryLanguage = null;

    @Nonnull
    public static DictionaryLanguageData get(@Nonnull IRI propertyIri,
                                             @Nonnull String browserText,
                                             @Nonnull String lang) {
        return new AutoValue_DictionaryLanguageData(propertyIri, browserText, lang);
    }

    @JsonCreator
    @Nonnull
    public static DictionaryLanguageData get(@Nonnull @JsonProperty(PROPERTY_IRI) IRI propertyIri,
                                             @Nonnull @JsonProperty(LANGUAGE_TAG) String languageTag) {
        return get(propertyIri, getBrowserText(propertyIri), languageTag);
    }

    public static DictionaryLanguageData rdfsLabel(@Nonnull String languageTag) {
        return get(RDFS_LABEL.getIRI(), languageTag);
    }

    private static String getBrowserText(@Nonnull IRI propertyIri) {
        return WellKnownLabellingIris.get(propertyIri)
                                     .map(l -> l.getPrefixedName())
                                     .orElse(propertyIri.toString());
    }

    @Nonnull
    public static DictionaryLanguageData getRdfsLabelWithLang(@Nonnull String lang) {
        return get(RDFS_LABEL.getIRI(), RDFS_LABEL.getPrefixedName(), lang);
    }



    @JsonProperty(PROPERTY_IRI)
    @Nonnull
    public abstract IRI getAnnotationPropertyIri();

    @JsonIgnore
    @Nonnull
    public abstract String getAnnotationPropertyBrowserText();

    @JsonProperty(LANGUAGE_TAG)
    @Nonnull
    public abstract String getLanguageTag();


    @JsonIgnore
    @Nonnull
    public DictionaryLanguage getDictionaryLanguage() {
        if(dictionaryLanguage == null) {
            dictionaryLanguage = createDictionaryLanguage();
        }
        return dictionaryLanguage;
    }


    @JsonIgnore
    @Nonnull
    public Optional<OWLAnnotationPropertyData> getAnnotationPropertyData() {
        return Optional.of(
                OWLAnnotationPropertyData.get(
                        DataFactory.getOWLAnnotationProperty(getAnnotationPropertyIri()),
                        getAnnotationPropertyBrowserText(),
                        ImmutableMap.of()
                )
        );
    }

    private DictionaryLanguage createDictionaryLanguage() {
        return DictionaryLanguage.create(getAnnotationPropertyIri(),
                                         getLanguageTag());
    }
}
