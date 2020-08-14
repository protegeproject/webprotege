package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import jsinterop.annotations.JsProperty;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-05
 */
@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName(AnnotationAssertionDictionaryLanguage.TYPE_NAME)
public abstract class AnnotationAssertionDictionaryLanguage extends DictionaryLanguage {

    public static final String TYPE_NAME = "AnnotationAssertion";

    protected static final String PROPERTY_IRI = "propertyIri";

    protected static final String LANG = "lang";


    @JsonCreator
    public static AnnotationAssertionDictionaryLanguage get(@Nullable @JsonProperty(PROPERTY_IRI) String propertyIri,
                                                            @Nullable @JsonProperty(LANG) String lang) {
        String normalisedLang;
        if(lang == null) {
            normalisedLang = "";
        }
        else {
            normalisedLang = lowerCaseLangTag(lang);
        }
        IRI iri;
        if(RDFS_LABEL.getPrefixedName().equals(propertyIri)) {
            iri = RDFS_LABEL.getIRI();
        }
        else if(propertyIri != null) {
            iri = IRI.create(propertyIri);
        }
        else {
            iri = null;
        }
        return new AutoValue_AnnotationAssertionDictionaryLanguage(iri, normalisedLang);
    }

    @Override
    public boolean isAnnotationBased() {
        return true;
    }

    @Nonnull
    public static AnnotationAssertionDictionaryLanguage get(@Nonnull @JsonProperty(PROPERTY_IRI) IRI propertyIri,
                                         @Nullable @JsonProperty(LANG) String lang) {
        return get(propertyIri.toString(), lang);
    }

    @Override
    public <R> R accept(@Nonnull DictionaryLanguageVisitor<R> visitor) {
        return visitor.visit(this);
    }


    @JsonIgnore
    @Nonnull
    public abstract IRI getAnnotationPropertyIri();

    @Nullable
    @JsonProperty(PROPERTY_IRI)
    public String getJsonAnnotationPropertyIri() {
        IRI annotationPropertyIri = getAnnotationPropertyIri();
        if(RDFS_LABEL.getIRI().equals(annotationPropertyIri)) {
            return RDFS_LABEL.getPrefixedName();
        }
        else {
            return annotationPropertyIri.toString();
        }
    }

    @JsonProperty(LANG)
    @Nonnull
    public abstract String getLang();
}
