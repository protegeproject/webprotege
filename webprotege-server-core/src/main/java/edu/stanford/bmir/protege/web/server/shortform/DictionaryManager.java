package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
@ProjectSingleton
public class DictionaryManager {

    @Nonnull
    private final LanguageManager languageManager;

    @Nonnull
    private final MultiLingualDictionary dictionary;

    @Nonnull
    private final BuiltInShortFormDictionary builtInShortFormDictionary;

    @Inject
    public DictionaryManager(@Nonnull LanguageManager languageManager,
                             @Nonnull MultiLingualDictionary dictionary,
                             @Nonnull BuiltInShortFormDictionary builtInShortFormDictionary) {
        this.languageManager = checkNotNull(languageManager);
        this.dictionary = checkNotNull(dictionary);
        this.builtInShortFormDictionary = checkNotNull(builtInShortFormDictionary);
    }

    /**
     * Gets the entities that exactly match the specified short form
     * @param shortForm The short form.  The short form may be quoted.  If it is, the match will
     *                  be performed on the non-quoted version of the specified short form.
     * @return The entities that exactly match the specified short form.
     */
    public Stream<OWLEntity> getEntities(@Nonnull String shortForm) {
        return dictionary.getEntities(ShortFormQuotingUtils.getUnquotedShortForm(shortForm),
                                      languageManager.getLanguages());
    }

    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull List<DictionaryLanguage> languages) {
        var builtInEntityShortForm = builtInShortFormDictionary.getShortForm(entity, null);
        if (builtInEntityShortForm != null) {
            return builtInEntityShortForm;
        }
        return dictionary.getShortForm(entity, languages, "");
    }

    @Nonnull
    public String getQuotedShortForm(@Nonnull OWLEntity entity,
                                     @Nonnull List<DictionaryLanguage> languages) {
        var shortForm = getShortForm(entity, languages);
        return ShortFormQuotingUtils.getQuotedShortForm(shortForm);
    }

    @Nonnull
    public String getQuotedShortForm(@Nonnull OWLEntity entity) {
        return getQuotedShortForm(entity,
                                  languageManager.getLanguages());
    }

    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity) {
        return getShortForm(entity,
                            languageManager.getLanguages());
    }

    /**
     * Gets the short forms containing the specified search strings.
     * @param searchStrings The search strings
     * @param entityTypes The types of entities to be matched.
     * @param languages The languages of the short forms.
     * @return A stream of matching short forms.
     */
    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                          @Nonnull Set<EntityType<?>> entityTypes,
                                                          @Nonnull List<DictionaryLanguage> languages) {
        return Streams.concat(
                builtInShortFormDictionary.getShortFormsContaining(searchStrings, entityTypes),
                dictionary.getShortFormsContaining(searchStrings, entityTypes, languages)
        );
    }

    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                          @Nonnull Set<EntityType<?>> entityTypes) {
        if(entityTypes.isEmpty()) {
            return Stream.empty();
        }
        return getShortFormsContaining(searchStrings, entityTypes, languageManager.getLanguages());
    }

    public void update(@Nonnull Collection<OWLEntity> entities) {
        dictionary.update(entities,
                          languageManager.getLanguages());
    }

    @Nonnull
    public ImmutableMap<DictionaryLanguage, String> getShortForms(OWLEntity entity) {
        var shortForm = builtInShortFormDictionary.getShortForm(entity, null);
        if(shortForm != null) {
            return ImmutableMap.of(DictionaryLanguage.localName(), shortForm);
        }
        return dictionary.getShortForms(entity, languageManager.getActiveLanguages());
    }
}
