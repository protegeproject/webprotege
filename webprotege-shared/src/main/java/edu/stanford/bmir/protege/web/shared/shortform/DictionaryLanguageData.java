package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class DictionaryLanguageData {

    private static final String PROPERTY_IRI = "propertyIri";

    private static final String LANGUAGE_TAG = "lang";

    private DictionaryLanguage dictionaryLanguage = null;

    @Nonnull
    public static DictionaryLanguageData get(@Nullable IRI propertyIri,
                                             @Nonnull String browserText,
                                             @Nullable String lang) {
        String normalisedLang;
        if(lang == null) {
            normalisedLang = "";
        }
        else {
            normalisedLang = lang.toLowerCase();
        }
        return new AutoValue_DictionaryLanguageData(propertyIri, browserText, normalisedLang);
    }

    @JsonCreator
    @Nonnull
    public static DictionaryLanguageData get(@Nullable @JsonProperty(PROPERTY_IRI) IRI propertyIri,
                                             @Nonnull @JsonProperty(LANGUAGE_TAG) String languageTag) {
        return get(propertyIri, getBrowserText(propertyIri), languageTag);
    }

    public static DictionaryLanguageData localName() {
        return get(null, "");
    }

    public static DictionaryLanguageData rdfsLabel(@Nonnull String languageTag) {
        return get(RDFS_LABEL.getIRI(), languageTag);
    }

    private static String getBrowserText(@Nullable IRI propertyIri) {
        if(propertyIri == null) {
            return "";
        }
        return WellKnownLabellingIris.get(propertyIri)
                                     .map(WellKnownLabellingIris::getPrefixedName)
                                     .orElse(propertyIri.toString());
    }

    @Nonnull
    public static DictionaryLanguageData getRdfsLabelWithLang(@Nonnull String lang) {
        return get(RDFS_LABEL.getIRI(), RDFS_LABEL.getPrefixedName(), lang);
    }



    @JsonProperty(PROPERTY_IRI)
    @Nullable
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
        IRI propertyIri = getAnnotationPropertyIri();
        if(propertyIri == null) {
            return Optional.empty();
        }
        return Optional.of(
                OWLAnnotationPropertyData.get(
                        DataFactory.getOWLAnnotationProperty(propertyIri),
                        ImmutableMap.of(
                                createDictionaryLanguage(), getBrowserText(propertyIri)
                        )
                )
        );
    }

    private DictionaryLanguage createDictionaryLanguage() {
        IRI propertyIri = getAnnotationPropertyIri();
        if(propertyIri == null) {
            return LocalNameDictionaryLanguage.get();
        }
        else {
            return AnnotationAssertionDictionaryLanguage.get(propertyIri,
                                                             getLanguageTag());
        }

    }

    @JsonIgnore
    public boolean isAnnotationBased() {
        return getAnnotationPropertyIri() != null;
    }
}
