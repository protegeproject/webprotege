package edu.stanford.bmir.protege.web.server.shortform;

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
public class DictionaryLanguage {

    private static final DictionaryLanguage LOCAL_NAME_LANGUAGE = new DictionaryLanguage(null, "");

    @Nullable
    private final IRI annotationPropertyIri;

    @Nonnull
    private final String lang;

    /**
     * Creates a {@link DictionaryLanguage} that is not for any annotation property or any lang.
     */
    @Nonnull
    public static DictionaryLanguage localName() {
        return LOCAL_NAME_LANGUAGE;
    }

    /**
     * Creates a {@link DictionaryLanguage} that is for the specified annotation property and lang
     * @param annotationPropertyIri The annotation property
     * @param lang The language.  May be empty.
     */
    @Nonnull
    public static DictionaryLanguage create(@Nonnull IRI annotationPropertyIri, @Nonnull String lang) {
        return new DictionaryLanguage(annotationPropertyIri, lang);
    }

    @Nonnull
    public static DictionaryLanguage rdfsLabel(@Nonnull String lang) {
        return DictionaryLanguage.create(RDFS_LABEL.getIRI(), lang);
    }

    @Nonnull
    public static DictionaryLanguage skosPrefLabel(@Nonnull String lang) {
        return DictionaryLanguage.create(SKOSVocabulary.PREFLABEL.getIRI(), lang);
    }

    private DictionaryLanguage(@Nullable IRI annotationPropertyIri, @Nonnull String lang) {
        this.annotationPropertyIri = annotationPropertyIri;
        this.lang = lang;
    }

    public boolean isAnnotationBased() {
        return !this.equals(LOCAL_NAME_LANGUAGE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DictionaryLanguage)) {
            return false;
        }
        DictionaryLanguage other = (DictionaryLanguage) obj;
        return Objects.equal(this.annotationPropertyIri, other.annotationPropertyIri)
                && this.lang.equals(other.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(annotationPropertyIri, lang);
    }


    @Override
    public String toString() {
        return toStringHelper("DictionaryLanguage")
                .add("prop", annotationPropertyIri)
                .add("lang", lang)
                .toString();
    }

    public boolean isLanguageFor(@Nonnull IRI annotationPropertyIri, @Nonnull String lang) {
        return Objects.equal(this.annotationPropertyIri, annotationPropertyIri)
                && this.lang.equals(lang);
    }
}
