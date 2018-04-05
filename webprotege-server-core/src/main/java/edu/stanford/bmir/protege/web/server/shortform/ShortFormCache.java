package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class ShortFormCache {

    private static final int DEFAULT_CAPACITY = 100;

    @Nonnull
    private final Multimap<String, IRI> shortForm2IriMap;

    @Nonnull
    private final Map<IRI, String> iri2ShortFormMap;

    private ShortFormCache(@Nonnull Multimap<String, IRI> shortForm2IriMap,
                            @Nonnull Map<IRI, String> iri2ShortFormMap) {
        this.shortForm2IriMap = checkNotNull(shortForm2IriMap);
        this.iri2ShortFormMap = checkNotNull(iri2ShortFormMap);
    }

    @Nonnull
    public static ShortFormCache create() {
        return createWithCapacity(DEFAULT_CAPACITY);
    }

    @Nonnull
    public static ShortFormCache createWithCapacity(int capacity) {
        return new ShortFormCache(HashMultimap.create(capacity, 1),
                                   new HashMap<>(capacity));
    }

    /**
     * Gets the number of IRIs that are mapped to short forms by this {@link DictionaryCache}
     */
    public int size() {
        return iri2ShortFormMap.size();
    }

    public void put(@Nonnull IRI iri, @Nonnull String shortForm) {
        iri2ShortFormMap.put(checkNotNull(iri), checkNotNull(shortForm));
        shortForm2IriMap.put(shortForm, iri);
    }

    public void remove(@Nonnull IRI iri) {
        String shortForm = iri2ShortFormMap.remove(checkNotNull(iri));
        if (shortForm != null) {
            shortForm2IriMap.removeAll(shortForm);
        }
    }

    public void clear() {
        shortForm2IriMap.clear();
        iri2ShortFormMap.clear();
    }

    public String getShortFormOrElse(@Nonnull IRI iri, @Nonnull Function<IRI, String> defaultShortFormSupplier) {
        String shortForm = iri2ShortFormMap.get(iri);
        if(shortForm == null) {
            return defaultShortFormSupplier.apply(iri);
        }
        else {
            return shortForm;
        }
    }

    @Nonnull
    public Collection<IRI> getIri(@Nonnull String shortForm) {
        return ImmutableList.copyOf(shortForm2IriMap.get(shortForm));
    }
}
