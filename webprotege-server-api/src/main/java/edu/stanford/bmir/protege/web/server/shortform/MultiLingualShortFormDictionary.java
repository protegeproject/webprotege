package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-02
 *
 * Provides short forms for entities given a list of dictionary languages
 */
public interface MultiLingualShortFormDictionary {
    /**
     * Gets a short form for the specified entity.  Dictionaries for the specified languages are examined
     * in the specified order.  This means that a short form for a language that appears earlier in the
     * list will be returned first.
     *
     * @param entity           The entity for which a short form is to be retrieved.
     * @param languages        The languages to be considered.
     * @param defaultShortForm The short form to be returned if none of the dictionaries corresponding to
     *                         the specified languages contain the specified short form.
     * @return The default short form to return in case no dictionary contains a short form for the specified
     * entity.
     */
    @Nonnull
    String getShortForm(@Nonnull OWLEntity entity,
                        @Nonnull List<DictionaryLanguage> languages,
                        @Nonnull String defaultShortForm);

    /**
     * Gets the short forms in the specified languages for the specified entity.
     *
     * @param entity    The entity
     * @param languages The list of languages to consider
     * @return A map of languages for short forms for known language short form mappings
     */
    @Nonnull
    ImmutableMap<DictionaryLanguage, String> getShortForms(@Nonnull OWLEntity entity,
                                                           @Nonnull List<DictionaryLanguage> languages);
}
