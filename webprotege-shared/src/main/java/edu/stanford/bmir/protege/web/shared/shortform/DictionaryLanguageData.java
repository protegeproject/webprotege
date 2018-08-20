package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

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

    private static final String PROPERTY_IRI = "propertyIri";

    private static final String LANGUAGE_TAG = "languageTag";

    @Nonnull
    public static DictionaryLanguageData get(@Nonnull IRI propertyIri,
                                             @Nonnull String browserText,
                                             @Nonnull String lang) {
        return new AutoValue_DictionaryLanguageData(propertyIri, browserText, lang);
    }

    @Nonnull
    public static DictionaryLanguageData get(@Nonnull IRI propertyIri,
                                             @Nonnull String languageTag) {
        return get(propertyIri, getBrowserText(propertyIri), languageTag);
    }

    private static String getBrowserText(@Nonnull IRI propertyIri) {
        if(propertyIri.equals(OWLRDFVocabulary.RDFS_LABEL.getIRI())) {
            return OWLRDFVocabulary.RDFS_LABEL.getPrefixedName();
        }
        else if(propertyIri.equals(SKOSVocabulary.PREFLABEL.getIRI())) {
            return SKOSVocabulary.PREFLABEL.getPrefixedName();
        }
        else {
            return "";
        }
    }
    
    @Nonnull
    public static DictionaryLanguageData getRdfsLabelWithLang(@Nonnull String lang) {
        return get(OWLRDFVocabulary.RDFS_LABEL.getIRI(), OWLRDFVocabulary.RDFS_LABEL.getPrefixedName(), lang);
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
    public Optional<OWLAnnotationPropertyData> getAnnotationPropertyData() {
        return Optional.of(
                OWLAnnotationPropertyData.get(
                        DataFactory.getOWLAnnotationProperty(getAnnotationPropertyIri()),
                        getAnnotationPropertyBrowserText(),
                        ImmutableMap.of()
                )
        );
    }

    public DictionaryLanguage toDictionaryLanguage() {
        return DictionaryLanguage.create(getAnnotationPropertyIri(),
                                         getLanguageTag());
    }
}
