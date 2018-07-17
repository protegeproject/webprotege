package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 *
 * A dictionary that maps entities to short forms for a given {@link DictionaryLanguage}.
 * This class is thread safe, but consistency is not guaranteed for multiple readers or writers.
 */
public class Dictionary {

    private static final int DEFAULT_CAPACITY = 100;

    @Nonnull
    private final ShortFormCache shortFormCache;

    @Nonnull
    private DictionaryLanguage language;

    private Dictionary(@Nonnull ShortFormCache shortFormCache,
                       @Nonnull DictionaryLanguage language) {
        this.shortFormCache = checkNotNull(shortFormCache);
        this.language = checkNotNull(language);
    }

    public static Dictionary create(@Nonnull DictionaryLanguage language) {
        return createWithCapacity(DEFAULT_CAPACITY, language);
    }

    public static Dictionary createWithCapacity(int capacity,
                                                @Nonnull DictionaryLanguage language) {
        return new Dictionary(ShortFormCache.createWithCapacity(capacity),
                              language);
    }

    /**
     * Gets the language that this dictionary is for.
     */
    @Nonnull
    public DictionaryLanguage getLanguage() {
        return language;
    }

    /**
     * Gets the number of entities that are mapped to short forms by this {@link Dictionary}
     */
    public int size() {
        return shortFormCache.size();
    }

    /**
     * Creates an entry in this dictionary that maps the specified entity to the specified short form.
     * @param entity The entity.
     * @param shortForm The short form.
     */
    public void put(@Nonnull OWLEntity entity,
                    @Nonnull String shortForm) {
        shortFormCache.put(checkNotNull(entity), checkNotNull(shortForm));
    }

    /**
     * Removes the specified entity from this dictionary.
     */
    public void remove(@Nonnull OWLEntity entity) {
        shortFormCache.remove(entity);
    }

    /**
     * Clears this dictionary of all entries.
     */
    public void clear() {
        shortFormCache.clear();
    }

    /**
     * Gets the short form for the specified entity.
     * @param entity The entity.
     * @param defaultShortForm A default short form that will be returned if this entity does not
     *                         contain an entry for the specified entity.
     */
    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull String defaultShortForm) {
        return shortFormCache.getShortFormOrElse(entity,
                                                 defaultShortForm);
    }

    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                          @Nonnull Set<EntityType<?>> entityTypes) {
        ShortFormMatchFunction supplier = (entity, shortForm, matchCount, matchPositions) ->
                new ShortFormMatch(entity, shortForm, language, matchCount, matchPositions);
        return shortFormCache.getShortFormsContaining(searchStrings,
                                                      entityTypes,
                                                      supplier);
    }

    @Nonnull
    public Stream<OWLEntity> getEntities(@Nonnull String shortForm) {
        return shortFormCache.getEntities(shortForm);
    }
}
