package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 *
 * A dictionary that supports look up for multiple languages
 */
@ProjectSingleton
public interface MultiLingualDictionary {

    /**
     * Causes the specified list of languages to be pre-loaded.
     * @param languages The list of languages that should be loaded.  After loading these languages will
     *                  be available for looking up short forms and entities.
     */
    void loadLanguages(@Nonnull List<DictionaryLanguage> languages);

    /**
     * Gets a short form for the specified entity.  Dictionaries for the specified languages are examined
     * in the specified order.  This means that a short form for a language that appears earlier in the
     * list will be returned first.
     * @param entity The entity for which a short form is to be retrieved.
     * @param languages The languages to be considered.
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
     * Gets short forms containing the specified search strings
     * @param searchStrings The search strings.
     * @param entityTypes The types of entities to be retrieved.
     * @param languages The list of languages to consider.
     * @return A stream of short forms.
     */
    @Nonnull
    Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                        @Nonnull Set<EntityType<?>> entityTypes,
                                        @Nonnull List<DictionaryLanguage> languages);

    /**
     * Gets a stream of entities that exactly match the specified short form.
     * @param shortForm The short form.
     * @param languages A list of languages.  All dictionaries that are for the specified languages will be examined.
     */
    Stream<OWLEntity> getEntities(@Nonnull String shortForm,
                                      @Nonnull List<DictionaryLanguage> languages);

    /**
     * Updates the dictionary entries for the specified entities.
     * @param entities The entities that will be updated.
     * @param languages The languages that should be updated.
     */
    void update(@Nonnull Collection<OWLEntity> entities,
                @Nonnull List<DictionaryLanguage> languages);

    /**
     * Gets the short forms for the specified entity.
     * @param entity The entity
     * @return A map of languages for short forms for known language short form mappings
     */
    @Nonnull
    ImmutableMap<DictionaryLanguage,String> getShortForms(OWLEntity entity);

    /**
     * Gets the short forms in the specified languages for the specified entity.
     */
    ImmutableMap<DictionaryLanguage,String> getShortForms(OWLEntity entity, List<DictionaryLanguage> languages);
}
