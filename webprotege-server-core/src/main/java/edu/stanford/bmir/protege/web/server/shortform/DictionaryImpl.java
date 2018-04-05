package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isDictionaryCacheFor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
public class DictionaryImpl implements Dictionary {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryImpl.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private DictionaryCacheUpdater cacheUpdater;

    @Nonnull
    private DictionaryCacheBuilder cacheBuilder;

    @Nonnull
    private final List<DictionaryCache> caches = new ArrayList<>();

    @Inject
    public DictionaryImpl(@Nonnull ProjectId projectId,
                          @Nonnull OWLOntology rootOntology,
                          @Nonnull DictionaryCacheBuilder cacheBuilder,
                          @Nonnull DictionaryCacheUpdater cacheUpdater) {
        this.projectId = checkNotNull(projectId);
        this.rootOntology = checkNotNull(rootOntology);
        this.cacheUpdater = checkNotNull(cacheUpdater);
        this.cacheBuilder = checkNotNull(cacheBuilder);
    }

    @Nonnull
    @Override
    public Collection<String> getLangs() {
        return caches.stream()
                     .map(DictionaryCache::getLang)
                     .collect(toSet());
    }

    @Nonnull
    @Override
    public Collection<OWLEntity> getEntities(@Nonnull IRI annotationPropertyIri,
                                             @Nonnull String preferredLang,
                                             @Nonnull String shortForm) {
        return caches.stream()
                     .flatMap(cache -> cache.getIri(shortForm).stream())
                     .flatMap(iri -> rootOntology.getEntitiesInSignature(iri, Imports.INCLUDED).stream())
                     .collect(toSet());
    }

    @Nonnull
    @Override
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull IRI annotationPropertyIri,
                               @Nonnull String lang, String defaultShortForm) {
        DictionaryCache prefLangCache = findOrCreateCache(annotationPropertyIri, lang);
        IRI iri = entity.getIRI();
        return prefLangCache.getShortFormOrElse(iri, (i) -> defaultShortForm);
    }

    @Override
    public void handleChanges(@Nonnull List<? extends OWLOntologyChange> changes) {
        caches.forEach(cache -> cacheUpdater.update(cache, changes));
    }

    /**
     * Finds an existing cache or creates a fresh cache for the specified annotation property IRI
     * and specified lang.
     * @param annotationPropertyIri The annotation property IRI
     * @param lang The lang
     */
    @Nonnull
    private DictionaryCache findOrCreateCache(@Nonnull IRI annotationPropertyIri,
                                              @Nonnull String lang) {
        for(DictionaryCache c : caches) {
            if(isDictionaryCacheFor(c, annotationPropertyIri, lang)) {
                return c;
            }
        }
        return createAndRegisterCache(annotationPropertyIri, lang);
    }

    /**
     * Creates and registers a cache for the specified property and language.
     * @param annotationPropertyIri The IRI of the annotation property that the cache is registered for.
     * @param lang The language that the cache is registered for.
     * @return The created cache.
     * @throws RuntimeException if a cache for the specified annotation property IRI is already registered.
     */
    private DictionaryCache createAndRegisterCache(@Nonnull IRI annotationPropertyIri,
                                                   @Nonnull String lang) {
        logger.info("{} Creating cache for <{}> and lang={}",
                    projectId,
                    annotationPropertyIri,
                    lang);
        Stopwatch stopwatch = Stopwatch.createStarted();
        DictionaryCache cache = cacheBuilder.build(annotationPropertyIri, lang);
        registerCache(cache);
        stopwatch.stop();
        logger.info("{} Created cache for <{}> and lang={} in {} ms",
                    projectId,
                    annotationPropertyIri,
                    lang,
                    stopwatch.elapsed(MILLISECONDS));
        return cache;
    }

    /**
     * Adds a cache to the list of caches.
     * @param cache The cache to be added.
     * @throws RuntimeException if the specified cache is already registered.
     */
    private void registerCache(@Nonnull DictionaryCache cache) {
        for(DictionaryCache c : caches) {
            if(c.getAnnotationPropertyIri().equals(cache.getAnnotationPropertyIri())
                    && c.getLang().equals(cache.getLang())) {
                throw new RuntimeException("Duplicate cache");
            }
        }
        caches.add(cache);
    }
}
