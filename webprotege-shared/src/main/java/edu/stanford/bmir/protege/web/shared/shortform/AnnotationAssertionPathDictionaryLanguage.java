package edu.stanford.bmir.protege.web.shared.shortform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionDictionaryLanguage.LANG;
import static edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionDictionaryLanguage.PROPERTY_IRI;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-05
 */
@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName(AnnotationAssertionPathDictionaryLanguage.TYPE_NAME)
public abstract class AnnotationAssertionPathDictionaryLanguage extends DictionaryLanguage {

    public static final String TYPE_NAME = "AnnotationAssertionPath";

    public static final String LANG = "lang";

    public static final String PATH = "path";


    @JsonCreator
    @Nonnull
    public static AnnotationAssertionPathDictionaryLanguage get(@JsonProperty(PATH) @Nonnull ImmutableList<IRI> path,
                                                                @JsonProperty(LANG) @Nonnull String newLang) {
        return new AutoValue_AnnotationAssertionPathDictionaryLanguage(path, newLang);
    }

    @Override
    public boolean isAnnotationBased() {
        return true;
    }

    @Override
    public <R> R accept(@Nonnull DictionaryLanguageVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @JsonProperty(PATH)
    public abstract ImmutableList<IRI> getAnnotationPropertyPath();

    /**
     * Gets the language tag.
     * @return The language tag string.  May be empty.
     */
    @JsonProperty(LANG)
    @Nonnull
    public abstract String getLang();

}
