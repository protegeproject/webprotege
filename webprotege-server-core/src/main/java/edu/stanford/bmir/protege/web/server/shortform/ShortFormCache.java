package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang.StringUtils.indexOfIgnoreCase;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class ShortFormCache {

    private static final int DEFAULT_CAPACITY = 100;

    @Nonnull
    private final Multimap<String, OWLEntity> shortForm2EntityMap;

    @Nonnull
    private final Map<OWLEntity, ShortForm> entity2ShortFormMap;

    @Inject
    public ShortFormCache() {
        this(DEFAULT_CAPACITY);
    }

    private ShortFormCache(int capacity) {
        this.shortForm2EntityMap = HashMultimap.create(capacity, 1);
        this.entity2ShortFormMap = new ConcurrentHashMap<>(DEFAULT_CAPACITY);
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

    public void put(@Nonnull OWLEntity entity, @Nonnull String shortForm) {
        entity2ShortFormMap.put(checkNotNull(entity), ShortForm.create(checkNotNull(shortForm)));
        shortForm2EntityMap.put(shortForm, entity);
    }

    public void putAll(@Nonnull Map<OWLEntity, String> shortForms) {
        shortForms.forEach((entity, sf) -> {
            entity2ShortFormMap.put(entity, ShortForm.create(sf));
            shortForm2EntityMap.put(sf, entity);
        });
    }

    public void remove(@Nonnull OWLEntity entity) {
        ShortForm shortForm = entity2ShortFormMap.remove(checkNotNull(entity));
        if (shortForm != null) {
            shortForm2EntityMap.removeAll(shortForm);
        }
    }

    public void clear() {
        shortForm2EntityMap.clear();
        entity2ShortFormMap.clear();
    }

    public String getShortFormOrElse(@Nonnull OWLEntity entity, @Nonnull Function<OWLEntity, String> defaultShortFormSupplier) {
        ShortForm shortForm = entity2ShortFormMap.get(entity);
        if (shortForm == null) {
            return defaultShortFormSupplier.apply(entity);
        }
        else {
            return shortForm.getShortForm();
        }
    }

    @Nonnull
    public Collection<OWLEntity> getEntities(@Nonnull String shortForm) {
        return ImmutableList.copyOf(shortForm2EntityMap.get(shortForm));
    }

    @Nonnull
    public Collection<String> getShortForms() {
        return new ArrayList<>(shortForm2EntityMap.keySet());
    }

    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<String> searchStrings,
                                                                        @Nonnull ShortFormMatchFunction matchFunction) {
        ImmutableList<String> lowerCaseSearchStrings = searchStrings.stream()
                .map(String::toLowerCase)
                .collect(ImmutableList.toImmutableList());
        return entity2ShortFormMap.entrySet().stream()
                                  .map(e -> {
                                      int firstMatchIndex = Integer.MAX_VALUE;
                                      for(String searchString : lowerCaseSearchStrings) {
                                          int index = e.getValue().indexOfIgnoreCase(searchString);
                                          if(index != -1) {
                                              if(index < firstMatchIndex) {
                                                  firstMatchIndex = index;
                                              }
                                          }
                                          else {
                                              return null;
                                          }
                                      }
                                      return matchFunction.createMatch(e.getKey(), e.getValue().getShortForm(), firstMatchIndex);
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
            if(lc.equals(shortForm)) {
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
