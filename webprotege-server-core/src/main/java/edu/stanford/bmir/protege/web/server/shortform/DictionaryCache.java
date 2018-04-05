package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryCache {

    private static final int DEFAULT_CAPACITY = 100;

    @Nonnull
    private final ShortFormCache shortFormCache;

    @Nonnull
    private final IRI annotationPropertyIri;

    @Nonnull
    private final String lang;

    private DictionaryCache(@Nonnull ShortFormCache shortFormCache,
                            @Nonnull IRI annotationPropertyIri,
                            @Nonnull String lang) {
        this.shortFormCache = checkNotNull(shortFormCache);
        this.annotationPropertyIri = checkNotNull(annotationPropertyIri);
        this.lang = checkNotNull(lang);
    }

    public static DictionaryCache create(@Nonnull IRI annotationPropertyIri,
                                         @Nonnull String lang) {
        return createWithCapacity(DEFAULT_CAPACITY, annotationPropertyIri, lang);
    }

    public static DictionaryCache createWithCapacity(int capacity,
                                                     @Nonnull IRI annotationPropertyIri,
                                                     @Nonnull String lang) {
        return new DictionaryCache(ShortFormCache.createWithCapacity(capacity),
                                   annotationPropertyIri,
                                   lang);
    }

    @Nonnull
    public IRI getAnnotationPropertyIri() {
        return annotationPropertyIri;
    }

    @Nonnull
    public String getLang() {
        return lang;
    }

    /**
     * Gets the number of IRIs that are mapped to short forms by this {@link DictionaryCache}
     */
    public int size() {
        return shortFormCache.size();
    }

    public void put(@Nonnull IRI iri, @Nonnull String shortForm) {
        shortFormCache.put(iri, shortForm);
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
