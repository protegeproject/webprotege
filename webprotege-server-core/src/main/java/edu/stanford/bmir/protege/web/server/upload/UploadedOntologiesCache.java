package edu.stanford.bmir.protege.web.server.upload;

import com.google.common.base.Ticker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.stanford.bmir.protege.web.server.project.Ontology;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
@ApplicationSingleton
public class UploadedOntologiesCache implements HasDispose {

    private static final Duration EXPIRATION_DURATION = Duration.ofMinutes(2);

    private static final Duration SWEEP_PERIOD = Duration.ofSeconds(10);

    private static Logger logger = LoggerFactory.getLogger(UploadedOntologiesCache.class);

    private final ScheduledExecutorService service;

    private final Cache<DocumentId, Collection<Ontology>> cache;

    @Nonnull
    private final UploadedOntologiesProcessor uploadedOntologiesProcessor;

    @Inject
    public UploadedOntologiesCache(@Nonnull UploadedOntologiesProcessor uploadedOntologiesProcessor,
                                   @Nonnull Ticker ticker,
                                   @Nonnull @UploadedOntologiesCacheService ScheduledExecutorService service) {
        this.uploadedOntologiesProcessor = checkNotNull(uploadedOntologiesProcessor);
        this.service = checkNotNull(service);
        checkNotNull(ticker);
        this.cache = CacheBuilder.newBuilder()
                                 .expireAfterAccess(EXPIRATION_DURATION)
                                 .ticker(ticker)
                                 .removalListener(notification -> logger.info("Uploaded ontologies for {} removed from cache", notification.getKey()))
                                 .build();
    }

    public void start() {
        logger.info("Starting uploaded ontologies cache with an expiration duration of {} ms", EXPIRATION_DURATION.toMillis());
        service.scheduleAtFixedRate(cache::cleanUp,
                                    EXPIRATION_DURATION.toMillis(),
                                    SWEEP_PERIOD.toMillis(),
                                    TimeUnit.MILLISECONDS);
    }

    public void dispose() {
        cache.invalidateAll();
    }

    @Nonnull
    public Collection<Ontology> getUploadedOntologies(@Nonnull DocumentId documentId) throws OWLOntologyCreationException, IOException {
        try {
            return cache.get(documentId, () -> loadUploadedOntologies(documentId));
        } catch(ExecutionException e) {
            var cause = e.getCause();
            if(cause instanceof OWLOntologyCreationException) {
                throw ((OWLOntologyCreationException) cause);
            }
            else if(cause instanceof IOException) {
                throw ((IOException) cause);
            }
            else {
                logger.info("Could not process uploaded ontologies due to error", e);
                return Collections.emptySet();
            }
        }
    }

    private Collection<Ontology> loadUploadedOntologies(@Nonnull DocumentId documentId) throws OWLOntologyCreationException, IOException {
        logger.info("Loading uploaded ontology {}, which was not cached", documentId);
        return uploadedOntologiesProcessor.getUploadedOntologies(documentId);
    }
}
