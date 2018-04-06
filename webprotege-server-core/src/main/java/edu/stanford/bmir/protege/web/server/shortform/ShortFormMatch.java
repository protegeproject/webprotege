package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2018
 */
public class ShortFormMatch {

    @Nonnull
    private final OWLEntity entity;

    @Nonnull
    private final String shortForm;

    @Nonnull
    private final DictionaryLanguage language;

    private final int firstMatchIndex;

    public ShortFormMatch(@Nonnull OWLEntity entity,
                          @Nonnull String shortForm,
                          @Nonnull DictionaryLanguage language, int firstMatchIndex) {
        this.entity = checkNotNull(entity);
        this.shortForm = checkNotNull(shortForm);
        this.language = checkNotNull(language);
        this.firstMatchIndex = firstMatchIndex;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public String getShortForm() {
        return shortForm;
    }

    public int getFirstMatchIndex() {
        return firstMatchIndex;
    }

    @Nonnull
    public DictionaryLanguage getLanguage() {
        return language;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity, shortForm, language);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ShortFormMatch)) {
            return false;
        }
        ShortFormMatch other = (ShortFormMatch) obj;
        return this.entity.equals(other.entity)
                && this.shortForm.equals(other.shortForm)
                && this.language.equals(other.language);
    }


    @Override
    public String toString() {
        return toStringHelper("ShortFormMatch")
                .add("entity", entity)
                .add("shortForm", shortForm)
                .add("language", language)
                .toString();
    }
}
