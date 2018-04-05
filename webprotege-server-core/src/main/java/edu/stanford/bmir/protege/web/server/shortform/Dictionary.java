package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 *
 * A dictionary that maps IRIs to short forms for a given {@link DictionaryLanguage}.
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
     * Gets the number of IRIs that are mapped to short forms by this {@link Dictionary}
     */
    public int size() {
        return shortFormCache.size();
    }

    public void put(@Nonnull IRI iri, @Nonnull String shortForm) {
        shortFormCache.put(iri, shortForm);
    }

    public void putAll(@Nonnull Map<IRI, String> shortForms) {
        shortFormCache.putAll(shortForms);
    }

    public void remove(@Nonnull IRI iri) {
        shortFormCache.remove(iri);
    }

    public void clear() {
        shortFormCache.clear();
    }



    public String getShortFormOrElse(@Nonnull IRI iri, @Nonnull Function<IRI, String> defaultShortFormSupplier) {
        return shortFormCache.getShortFormOrElse(iri, defaultShortFormSupplier);
    }

    @Nonnull
    public Collection<IRI> getIri(@Nonnull String shortForm) {
        return ImmutableList.copyOf(shortFormCache.getIri(shortForm));
    }
}
