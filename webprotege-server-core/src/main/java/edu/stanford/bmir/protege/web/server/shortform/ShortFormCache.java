package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.*;
import com.google.common.primitives.ImmutableIntArray;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 *
 * Instances of this class are threadsafe.  Note, however, that consistency is not guaranteed when
 * iterating over results.  Short forms may be added or removed or changed whilst readers are iterating
 * over results.
 */
public class ShortFormCache {

    private static final int DEFAULT_CAPACITY = 100;

    @Nonnull
    private final Map<OWLEntity, ShortForm> entity2ShortFormMap;

    @Nonnull
    private final Multimap<String, OWLEntity> shortForm2EntityMap;


    @Inject
    public ShortFormCache() {
        this(DEFAULT_CAPACITY);
    }

    private ShortFormCache(int capacity) {
        this.entity2ShortFormMap = new ConcurrentHashMap<>(DEFAULT_CAPACITY);
        // There is likely to be one short form per entity.  Hash Multimaps are wasteful here
        // so we're using a list multi-map with a capacity of one.
        this.shortForm2EntityMap = Multimaps.synchronizedMultimap(ArrayListMultimap.create(capacity, 1));

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
     * Gets the number of OWL Entities that are mapped to short forms by this {@link Dictionary}
     */
    public int size() {
        return entity2ShortFormMap.size();
    }

    /**
     * Adds an entry that maps the specified entity to the specified short form.
     *
     * @param entity    The entity.
     * @param shortForm The short form.
     */
    public void put(@Nonnull OWLEntity entity,
                    @Nonnull String shortForm) {
        entity2ShortFormMap.put(checkNotNull(entity), ShortForm.create(checkNotNull(shortForm)));
        shortForm2EntityMap.put(shortForm, entity);
    }

    /**
     * Adds entries for all of the entries contains in the specified map of entities to short forms.
     */
    public void putAll(@Nonnull Map<OWLEntity, String> shortForms) {

        shortForms.forEach((entity, sf) -> {
            entity2ShortFormMap.put(entity, ShortForm.create(sf));
            shortForm2EntityMap.put(sf, entity);
        });

    }

    /**
     * Removes the entry for the specified entity.
     */
    public void remove(@Nonnull OWLEntity entity) {
        ShortForm shortForm = entity2ShortFormMap.remove(checkNotNull(entity));
        if (shortForm != null) {
            shortForm2EntityMap.removeAll(shortForm);
        }
    }

    /**
     * Clears this cache.
     */
    public void clear() {
        shortForm2EntityMap.clear();
        entity2ShortFormMap.clear();
    }

    /**
     * Gets the short form for the specified entity.  If no such short form exists then the specified
     * default short form is returned.
     */
    public String getShortFormOrElse(@Nonnull OWLEntity entity,
                                     @Nullable String defaultShortForm) {
        ShortForm shortForm = entity2ShortFormMap.get(entity);
        if (shortForm == null) {
            return defaultShortForm;
        }
        else {
            return shortForm.getShortForm();
        }
    }

    /**
     * Gets the entities that have the specified short form.  This must be an exact match.
     *
     * @param shortForm The short form.
     */
    @Nonnull
    public Stream<OWLEntity> getEntities(@Nonnull String shortForm) {
        return ImmutableList.copyOf(shortForm2EntityMap.get(shortForm)).stream();
    }

    /**
     * Gets the short forms matching the specified search strings.  The returned short forms match all
     * the specified search strings.
     *
     * @param searchStrings The search strings.
     * @param entityTypes   The types of entities to be matched.  If empty then no entities will be matched.
     * @param matchFunction A function that produces a {@link ShortFormMatch}
     * @return A stream of short form matches that match the specified search strings.
     */
    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                          @Nonnull Set<EntityType<?>> entityTypes,
                                                          @Nonnull ShortFormMatchFunction matchFunction) {
        if (entityTypes.isEmpty()) {
            return Stream.empty();
        }
        boolean matchAllEntityTypes = entityTypes.containsAll(EntityType.values());
        final int [] matchPositions = new int [searchStrings.size()];
        return entity2ShortFormMap.entrySet().stream()
                                  .filter(e -> matchAllEntityTypes || entityTypes.contains(e.getKey().getEntityType()))
                                  .map(e -> {
                                      ShortForm shortForm = e.getValue();
                                      Scanner scanner = new Scanner(shortForm.shortForm,
                                                                    shortForm.lowerCaseShortForm);
                                      int matchCount = 0;
                                      for (int i = 0; i < searchStrings.size(); i++) {
                                          SearchString searchString = searchStrings.get(i);
                                          int index = scanner.indexOf(searchString, 0);
                                          matchPositions[i] = index;
                                          if (index == -1) {
                                              // Search is boolean AND
                                              return null;
                                          }
                                          matchCount++;
                                      }
                                      if (matchCount > 0) {
                                          return matchFunction.createMatch(e.getKey(),
                                                                           shortForm.getShortForm(),
                                                                           matchCount,
                                                                           ImmutableIntArray.copyOf(matchPositions));
                                      }
                                      else {
                                          return null;
                                      }
                                  })
                                  .filter(Objects::nonNull);
    }

    private static class ShortForm {

        private final String shortForm;

        private final String lowerCaseShortForm;

        private ShortForm(String shortForm, String lowerCaseShortForm) {
            this.shortForm = shortForm;
            this.lowerCaseShortForm = lowerCaseShortForm;
        }

        public static ShortForm create(@Nonnull String shortForm) {
            String lc = shortForm.toLowerCase();
            String lowerCaseShortForm;
            if (lc.equals(shortForm)) {
                lowerCaseShortForm = shortForm;
            }
            else {
                lowerCaseShortForm = lc;
            }
            return new ShortForm(shortForm, lowerCaseShortForm);
        }

        public String getShortForm() {
            return shortForm;
        }

        public int indexOfIgnoreCase(@Nonnull String searchString) {
            return lowerCaseShortForm.indexOf(searchString);
        }

        @Override
        public int hashCode() {
            return shortForm.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ShortForm)) {
                return false;
            }
            ShortForm other = (ShortForm) obj;
            return this.shortForm.equals(other.shortForm);
        }
    }

}
