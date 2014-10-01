package edu.stanford.bmir.protege.web.server.reasoning;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.*;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.reasoning.*;
import edu.stanford.protege.reasoning.action.ReasonerState;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 29/09/2014
 */
public class ProjectReasoningServiceImpl implements ProjectReasoningService {

    private final WebProtegeLogger logger = WebProtegeLoggerManager.get(ProjectReasoningServiceImpl.class);



    private final ProjectId projectId;

    private final ReasoningService reasoningService;

    private final Map<KbDigest, ReasonerState> reasonerStateMap = Maps.newHashMap();

    private final Map<KbDigest, Throwable> errors = Maps.newHashMap();

    private final ReasoningServerSynchronizer reasonerSynchronizer;



    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock reasonerSynchronizationReadLock = readWriteLock.readLock();

    private final Lock reasonerSynchronizationWriteLock = readWriteLock.writeLock();


    private Optional<KbDigest> lastSyncedDigest = Optional.absent();


    private ListenableFuture<KbDigest> synchronizeReasonerFuture;



    public ProjectReasoningServiceImpl(
            ProjectId projectId, ReasoningService reasoningService, ReasoningServerSynchronizer reasonerSynchronizer) {
        this.projectId = projectId;
        this.reasoningService = reasoningService;
        this.reasonerSynchronizer = reasonerSynchronizer;
    }

    @Override
    public Optional<KbDigest> getLastSyncedDigest() {
        return lastSyncedDigest;
    }

    @Override
    public Optional<ReasonerState> getReasonerState(KbDigest digest) {
        return Optional.fromNullable(reasonerStateMap.get(digest));
    }

    @Override
    public <A extends Action<R, ?>, R extends Response> ListenableFuture<R> executeQuery(final A action) {
        logger.info(projectId, "Received request to execute a query: %s.", action);
        logger.info(projectId, "Last synced digest: %s.", lastSyncedDigest);
        if (lastSyncedDigest.isPresent()) {
            return executeQueryInternal(action);
        }
        else {
            synchronizeReasoner();
            return waitForSynchronizationAndExecuteQuery(action);
        }
    }

    @Override
    public void processOntologyChanges(List<OWLOntologyChange> changeList) {
        logger.info(projectId, "Passing ontology changes to reasoner synchronizer.");
        reasonerSynchronizer.handleOntologyChanges(changeList);
    }

    private void synchronizeReasoner() {
        try {
            reasonerSynchronizationWriteLock.lock();
            if(synchronizeReasonerFuture == null) {
                synchronizeReasonerFuture = reasonerSynchronizer.synchronizeReasoner();
                final Stopwatch stopwatch = Stopwatch.createStarted();
                Futures.addCallback(synchronizeReasonerFuture, new FutureCallback<KbDigest>() {
                    @Override
                    public void onSuccess(@Nullable KbDigest result) {
                        lastSyncedDigest = Optional.of(result);
                        // TODO: Post result?
                        logger.info(projectId,
                                    "Reasoner synchronization succeeded for digest %s. (Elapsed time: %s ms)",
                                    result,
                                    stopwatch.elapsed(TimeUnit.MILLISECONDS));
                        clearSynchronizationFuture();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: Post result
                        logger.info(projectId,
                                    "Reasoner synchronization failed.  Cause %s. (Elapsed time: %s ms)",
                                    t.getMessage(),
                                    stopwatch.elapsed(TimeUnit.MILLISECONDS));
                        // Either a timeout or reasoner internal error
                        if(t instanceof ReasonerTimeOutException) {
                            // TODO: Add to error
                        }
                        else if(t instanceof ReasonerInternalErrorException) {
                            // TODO: Add to error
                        }
                        else {
                            // Something else.
                        }
                        clearSynchronizationFuture();
                    }
                });
            }
        } finally {
            reasonerSynchronizationWriteLock.unlock();
        }
    }

    private void clearSynchronizationFuture() {
        reasonerSynchronizationWriteLock.lock();
        synchronizeReasonerFuture = null;
        reasonerSynchronizationWriteLock.unlock();
    }

    private <A extends Action<R, ?>, R extends Response> ListenableFuture<R> waitForSynchronizationAndExecuteQuery(final A action) {

        try {
            reasonerSynchronizationReadLock.lock();
            if(synchronizeReasonerFuture == null) {
                return executeQueryInternal(action);
            }
            else {
                final SettableFuture<R> result = SettableFuture.create();
                Futures.addCallback(synchronizeReasonerFuture, new FutureCallback<KbDigest>() {
                    @Override
                    public void onSuccess(@Nullable KbDigest r) {
                        ListenableFuture<R> response = executeQueryInternal(action);
                        Futures.addCallback(response, new FutureCallback<R>() {
                            @Override
                            public void onSuccess(@Nullable R r) {
                                result.set(r);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                result.setException(t);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Synchronization failed!
                        result.setException(t);
                    }
                });
                return result;
            }
        } finally {
            reasonerSynchronizationReadLock.unlock();
        }
    }

    private <R extends Response> ListenableFuture<R> executeQueryInternal(Action action) {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        ListenableFuture<R> responseFuture = reasoningService.execute(action);
        final SettableFuture<R> result = SettableFuture.create();
        Futures.addCallback(responseFuture, new FutureCallback<R>() {
            @Override
            public void onSuccess(@Nullable R r) {
                logger.info(projectId,
                            "Query execution success (elapsed time since submitting the query: %d ms).",
                            stopwatch.elapsed(TimeUnit.MILLISECONDS));
                result.set(r);
            }

            @Override
            public void onFailure(Throwable t) {
                logger.info(projectId, "Query execution failure (elapsed time since submitting the query: %d ms): %s.",
                            stopwatch.elapsed(TimeUnit.MILLISECONDS),
                            t.getMessage());
                result.setException(t);
            }
        });
        return result;
    }


}
