package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class DictionaryLanguage {

    private static final DictionaryLanguage LOCAL_NAME_LANGUAGE;

    private static final String PROPERTY_IRI = "propertyIri";

    private static final String LANG = "lang";

    static {
        LOCAL_NAME_LANGUAGE = create(null, "");
    }

    /**
     * Creates a {@link DictionaryLanguage} that is not for any annotation property or any lang.
     */
    @Nonnull
    public static DictionaryLanguage localName() {
        return LOCAL_NAME_LANGUAGE;
    }


    /**
     * Creates a {@link DictionaryLanguage} that is for the specified annotation property and lang
     *
     * @param annotationPropertyIri The annotation property
     * @param lang                  The language.  May be empty.  This will be normalised to a lower case string
     */
    @Nonnull
    public static DictionaryLanguage create(@Nullable @JsonProperty(PROPERTY_IRI) IRI annotationPropertyIri,
                                            @Nonnull @JsonProperty(LANG) String lang) {
        return new AutoValue_DictionaryLanguage(annotationPropertyIri, lang.toLowerCase());
    }

    /**
     * Creates a {@link DictionaryLanguage} that is for the specified annotation property and lang
     *
     * @param annotationPropertyIri The annotation property
     * @param lang                  The language.  May be empty.  This will be normalised to a lower case string
     */
    @JsonCreator
    @Nonnull
    private static DictionaryLanguage createFromJson(@Nullable @JsonProperty(PROPERTY_IRI) IRI annotationPropertyIri,
                                                     @Nullable @JsonProperty(LANG) String lang) {
        String normalisedLang;
        if(lang == null) {
            normalisedLang = "";
        }
        else {
            normalisedLang = lang.toLowerCase();
        }
        return new AutoValue_DictionaryLanguage(annotationPropertyIri, normalisedLang);
    }

    @Nonnull
    public static DictionaryLanguage rdfsLabel(@Nonnull String lang) {
        return DictionaryLanguage.create(RDFS_LABEL.getIRI(), lang);
    }

    @Nonnull
    public static DictionaryLanguage skosPrefLabel(@Nonnull String lang) {
        return DictionaryLanguage.create(SKOSVocabulary.PREFLABEL.getIRI(), lang);
    }

    @JsonProperty(PROPERTY_IRI)
    @Nullable
    public abstract IRI getAnnotationPropertyIri();

    @JsonProperty(LANG)
    @Nonnull
    public abstract String getLang();

    @JsonIgnore
    public boolean isAnnotationBased() {
        return !this.equals(LOCAL_NAME_LANGUAGE);
    }

    public boolean matches(@Nonnull IRI annotationPropertyIri, @Nonnull String lang) {
        return matchesAnnotationProperty(annotationPropertyIri)
                && matchesLang(lang);
    }

    public boolean equalsIgnoreLangCase(@Nonnull DictionaryLanguage language) {
        return Objects.equal(this.getAnnotationPropertyIri(), language.getAnnotationPropertyIri())
                && this.getLang().equalsIgnoreCase(language.getLang());
    }

    private boolean matchesLang(@Nonnull String lang) {
        return this.getLang().equals(lang) || this.getLang().equals("*");
    }

    private boolean matchesAnnotationProperty(@Nonnull IRI annotationPropertyIri) {
        return Objects.equal(this.getAnnotationPropertyIri(), annotationPropertyIri);
    }
}
