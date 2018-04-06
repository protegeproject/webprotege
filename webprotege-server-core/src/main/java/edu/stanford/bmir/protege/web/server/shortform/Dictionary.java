package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 *
 * A dictionary that maps entities to short forms for a given {@link DictionaryLanguage}.
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

    public void put(@Nonnull OWLEntity entity, @Nonnull String shortForm) {
        shortFormCache.put(entity, shortForm);
    }

    public void remove(@Nonnull OWLEntity entity) {
        shortFormCache.remove(entity);
    }

    public void clear() {
        shortFormCache.clear();
    }

    public Collection<String> getShortForms() {
        return shortFormCache.getShortForms();
    }

    public String getShortFormOrElse(@Nonnull OWLEntity entity, @Nonnull Function<OWLEntity, String> defaultShortFormSupplier) {
        return shortFormCache.getShortFormOrElse(entity, defaultShortFormSupplier);
    }

    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<String> searchStrings) {
        return shortFormCache.getShortFormsContaining(searchStrings, (entity, shortForm, firstMatchIndex) ->
                new ShortFormMatch(entity, shortForm, language, firstMatchIndex));
    }

    @Nonnull
    public Collection<OWLEntity> getEntities(@Nonnull String shortForm) {
        return ImmutableList.copyOf(shortFormCache.getEntities(shortForm));
    }
}
