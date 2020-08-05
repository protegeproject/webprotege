package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.obo.OboId;
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
@GwtCompatible(serializable = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(AnnotationAssertionDictionaryLanguage.class),
        @JsonSubTypes.Type(AnnotationAssertionPathDictionaryLanguage.class),
        @JsonSubTypes.Type(LocalNameDictionaryLanguage.class),
        @JsonSubTypes.Type(OboIdDictionaryLanguage.class),
        @JsonSubTypes.Type(PrefixedNameDictionaryLanguage.class)
})
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
        return new AutoValue_DictionaryLanguage(annotationPropertyIri, lowerCaseLangTag(lang));
    }

    /**
     * Creates a {@link DictionaryLanguage} that is for the specified annotation property and lang
     *
     * @param annotationPropertyIri The annotation property
     * @param lang                  The language.  May be empty.  This will be normalised to a lower case string
     */
    @JsonCreator
    @Nonnull
    private static DictionaryLanguage createFromJson(@Nullable @JsonProperty(PROPERTY_IRI) String annotationPropertyIri,
                                                     @Nullable @JsonProperty(LANG) String lang) {
        String normalisedLang;
        if(lang == null) {
            normalisedLang = "";
        }
        else {
            normalisedLang = lowerCaseLangTag(lang);
        }
        IRI iri;
        if(RDFS_LABEL.getPrefixedName().equals(annotationPropertyIri)) {
            iri = RDFS_LABEL.getIRI();
        }
        else if(annotationPropertyIri != null) {
            iri = IRI.create(annotationPropertyIri);
        }
        else {
            iri = null;
        }
        return new AutoValue_DictionaryLanguage(iri, normalisedLang);
    }

    @Nonnull
    public static DictionaryLanguage rdfsLabel(@Nonnull String lang) {
        return DictionaryLanguage.create(RDFS_LABEL.getIRI(), lang);
    }

    @Nonnull
    public static DictionaryLanguage skosPrefLabel(@Nonnull String lang) {
        return DictionaryLanguage.create(SKOSVocabulary.PREFLABEL.getIRI(), lang);
    }

    @JsonIgnore
    @Nullable
    public abstract IRI getAnnotationPropertyIri();

    @Nullable
    @JsonProperty(PROPERTY_IRI)
    public String getJsonAnnotationPropertyIri() {
        IRI annotationPropertyIri = getAnnotationPropertyIri();
        if(annotationPropertyIri == null) {
            return null;
        }
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

    public static String lowerCaseLangTag(String langTag) {
        for(int i = 0; i < langTag.length(); i++) {
            char ch = langTag.charAt(i);
            if((ch < 'a' || ch > 'z') && ch != '-') {
                return langTag.toLowerCase();
            }
        }
        return langTag;
    }

    public abstract <R> R accept(@Nonnull DictionaryLanguageVisitor<R> visitor);
}
