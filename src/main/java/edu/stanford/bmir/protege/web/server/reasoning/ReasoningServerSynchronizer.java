package edu.stanford.bmir.protege.web.server.reasoning;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.*;
import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.reasoning.ReasonerReadyEvent;
import edu.stanford.protege.reasoning.KbDigest;
import edu.stanford.protege.reasoning.ReasoningService;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/09/2014
 */
public class ReasoningServerSynchronizer {

    private final WebProtegeLogger logger = WebProtegeLoggerManager.get(ReasoningServerSynchronizer.class);

    private final ProjectId projectId;

    private final ReasoningService reasoningService;

    private final HasLogicalAxioms hasLogicalAxioms;

    private final List<AxiomChangeData> currentChangeList = Lists.newArrayList();

    private final HasPostEvents<ProjectEvent<?>> postEvents;

    private final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors
                                                                                                      .newSingleThreadExecutor());

    private Optional<KbDigest> lastSyncedDigest;

    private final Lock writeLock = new ReentrantLock();

    public ReasoningServerSynchronizer(
            ProjectId projectId,
            HasLogicalAxioms hasLogicalAxioms,
            ReasoningService reasoningService,
            HasPostEvents<ProjectEvent<?>> postEvents) {
        this.projectId = projectId;
        this.hasLogicalAxioms = hasLogicalAxioms;
        this.reasoningService = reasoningService;
        this.postEvents = postEvents;
    }

    public void handleOntologyChanges(List<? extends OWLOntologyChange> changes) {
        try {
            writeLock.lock();
            for (OWLOntologyChange change : changes) {
                if (change.isAxiomChange()) {
                    OWLAxiom ax = change.getAxiom();
                    if (ax.isLogicalAxiom()) {
                        currentChangeList.add((AxiomChangeData) change.getChangeData());
                    }
                }
            }
            if (!currentChangeList.isEmpty()) {
                synchronizeReasoner();
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Requests that the reasoner is synchronized with the latest set of logical axioms.
     * Note that reasoner synchronization happens asynchronously and this method returns immediately.
     */
    public ListenableFuture<KbDigest> synchronizeReasoner() {
        try {
            writeLock.lock();
            ImmutableList<AxiomChangeData> changesToApply = ImmutableList.copyOf(currentChangeList);

            ImmutableSet<OWLLogicalAxiom> expectedLogicalAxioms = ImmutableSet.copyOf(hasLogicalAxioms
                                                                                              .getLogicalAxioms());
            currentChangeList.clear();
            ListenableFuture<KbDigest> future = executorService.submit(new ReasonerSynchronizationTask(projectId,
                                                                                                       reasoningService,
                                                                                                       lastSyncedDigest,
                                                                                                       changesToApply,
                                                                                                       expectedLogicalAxioms));
            final SettableFuture<KbDigest> syncResult = SettableFuture.create();
            Futures.addCallback(future, new FutureCallback<KbDigest>() {
                @Override
                public void onSuccess(KbDigest result) {
                    setLastSyncedDigest(Optional.of(result));
                    syncResult.set(result);
                    postEvents.postEvent(new ReasonerReadyEvent(projectId));
                }

                @Override
                public void onFailure(Throwable t) {
                    logger.info(projectId, "There was an error when synchronizing the reasoner: ", t.getMessage());
                    setLastSyncedDigest(Optional.<KbDigest>absent());
                    syncResult.setException(t);
                    postEvents.postEvent(new ReasonerReadyEvent(projectId));
                }
            });
            return syncResult;
        } finally {
            writeLock.unlock();
        }
    }

    private void setLastSyncedDigest(Optional<KbDigest> result) {
        try {
            writeLock.lock();
            lastSyncedDigest = result;
        } finally {
            writeLock.unlock();
        }
    }
}
