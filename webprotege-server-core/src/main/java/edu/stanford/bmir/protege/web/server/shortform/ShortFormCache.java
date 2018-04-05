package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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

    @Inject
    public ShortFormCache() {
        this(DEFAULT_CAPACITY);
    }

    private ShortFormCache(int capacity) {
        this.shortForm2IriMap = HashMultimap.create(capacity, 1);
        this.iri2ShortFormMap = new HashMap<>(DEFAULT_CAPACITY);
    }

    @Nonnull
    public static ShortFormCache create() {
        return createWithCapacity(DEFAULT_CAPACITY);
    }

    @Nonnull
    public static ShortFormCache createWithCapacity(int capacity) {
        return new ShortFormCache(capacity);
    }

    /**
     * Gets the number of IRIs that are mapped to short forms by this {@link Dictionary}
     */
    public int size() {
        return iri2ShortFormMap.size();
    }

    public void put(@Nonnull IRI iri, @Nonnull String shortForm) {
        iri2ShortFormMap.put(checkNotNull(iri), checkNotNull(shortForm));
        shortForm2IriMap.put(shortForm, iri);
    }

    public void putAll(@Nonnull Map<IRI, String> shortForms) {
        shortForms.forEach((iri, sf) -> {
            iri2ShortFormMap.put(iri, sf);
            shortForm2IriMap.put(sf, iri);
        });
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
