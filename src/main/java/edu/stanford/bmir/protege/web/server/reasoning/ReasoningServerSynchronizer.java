package edu.stanford.bmir.protege.web.server.reasoning;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.util.concurrent.*;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.reasoning.KbDigest;
import edu.stanford.protege.reasoning.KbId;
import edu.stanford.protege.reasoning.ReasoningService;
import edu.stanford.protege.reasoning.util.MinimizedLogicalAxiomChanges;
import edu.stanford.protege.reasoning.util.ReasonerSynchronizer;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/09/2014
 */
public class ReasoningServerSynchronizer {

    private final WebProtegeLogger logger = WebProtegeLoggerManager.get(ReasoningServerSynchronizer.class);

    private final ProjectId projectId;

    private final HasLogicalAxioms hasLogicalAxioms;

    private final List<OWLOntologyChange> currentChangeList = Lists.newArrayList();

    private final ReasonerSynchronizer reasonerSynchronizer;

    private final Lock lock = new ReentrantLock();

    public ReasoningServerSynchronizer(
            ProjectId projectId,
            HasLogicalAxioms hasLogicalAxioms,
            ReasoningService reasoningService) {
        this.projectId = projectId;
        this.hasLogicalAxioms = hasLogicalAxioms;
        this.reasonerSynchronizer = new ReasonerSynchronizer(new KbId(projectId.getId()), reasoningService);
    }

    public ListenableFuture<KbDigest> handleOntologyChanges(List<? extends OWLOntologyChange> changes) {
        try {
            lock.lock();
            currentChangeList.addAll(changes);
            // Flush immediately?!?
            return synchronizeReasoner();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Requests that the reasoner is synchronized with the latest set of logical axioms.
     * Note that reasoner synchronization happens asynchronously and this method returns immediately.
     */
    public ListenableFuture<KbDigest> synchronizeReasoner() {
        try {
            lock.lock();
            ImmutableList<OWLOntologyChange> ontologyChanges = ImmutableList.copyOf(currentChangeList);
            MinimizedLogicalAxiomChanges changes = MinimizedLogicalAxiomChanges.fromOntologyChanges(ontologyChanges);
            ImmutableSortedSet<OWLLogicalAxiom> expectedLogicalAxioms = ImmutableSortedSet.copyOf(hasLogicalAxioms
                                                                                                    .getLogicalAxioms());
            return reasonerSynchronizer.synchronizeReasoner(changes, expectedLogicalAxioms);
        } finally {
            lock.unlock();
        }
    }

//        try {
//            writeLock.lock();
//            ImmutableList<AxiomChangeData> changesToApply = ImmutableList.copyOf(currentChangeList);
//
//            currentChangeList.clear();
//            ListenableFuture<KbDigest> future = executorService.submit(new ReasonerSynchronizationTask(projectId,
//                                                                                                       reasoningService,
//                                                                                                       lastSyncedDigest,
//                                                                                                       changesToApply,
//                                                                                                       expectedLogicalAxioms));
//            final SettableFuture<KbDigest> syncResult = SettableFuture.create();
//            Futures.addCallback(future, new FutureCallback<KbDigest>() {
//                @Override
//                public void onSuccess(KbDigest result) {
//                    setLastSyncedDigest(Optional.of(result));
//                    syncResult.set(result);
////                    postEvents.postEvent(new ReasonerReadyEvent(projectId));
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    Throwable exceptionToThrow;
//                    if(t instanceof ExecutionException) {
//                        exceptionToThrow = t.getCause();
//                    }
//                    else {
//                        exceptionToThrow = t;
//                    }
//                    logger.info(projectId,
//                                "An error occurred whilst synchronizing the reasoner: %s",
//                                exceptionToThrow.getMessage());
//                    setLastSyncedDigest(Optional.<KbDigest>absent());
//                    syncResult.setException(exceptionToThrow);
////                    postEvents.postEvent(new ReasonerReadyEvent(projectId));
//                }
//            });
//            return syncResult;
//        } finally {
//            writeLock.unlock();
//        }
//    }
//
//    private void setLastSyncedDigest(Optional<KbDigest> result) {
//        try {
//            writeLock.lock();
//            lastSyncedDigest = result;
//        } finally {
//            writeLock.unlock();
//        }
//    }
}
