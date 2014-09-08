package edu.stanford.bmir.protege.web.server.reasoning;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.*;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.reasoning.KbDigest;
import edu.stanford.protege.reasoning.ReasoningService;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;

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

    private final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
            Executors.newSingleThreadExecutor()
    );

    private Optional<KbDigest> lastSyncedDigest;

    private final Lock writeLock = new ReentrantLock();

    public ReasoningServerSynchronizer(
            ProjectId projectId, HasLogicalAxioms hasLogicalAxioms, ReasoningService reasoningService) {
        this.projectId = projectId;
        this.hasLogicalAxioms = hasLogicalAxioms;
        this.reasoningService = reasoningService;
    }

    public void handleOntologyChanges(List<? extends OWLOntologyChange> changes) {
        try {
            writeLock.lock();
            for(OWLOntologyChange change : changes) {
                if(change.isAxiomChange()) {
                    OWLAxiom ax = change.getAxiom();
                    if(ax.isLogicalAxiom()) {
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
    public void synchronizeReasoner() {
        try {
            writeLock.lock();
            ImmutableList<AxiomChangeData> changesToApply = ImmutableList.copyOf(currentChangeList);
            logger.info(projectId, "Flushing changes to reasoner: %s", changesToApply);

            ImmutableSet<OWLLogicalAxiom> expectedLogicalAxioms = ImmutableSet.copyOf(hasLogicalAxioms
                                                                                              .getLogicalAxioms());
            currentChangeList.clear();
            ListenableFuture<Optional<KbDigest>> future = executorService.submit(
                    new ReasonerSynchronizationTask(
                            projectId, reasoningService, lastSyncedDigest, changesToApply, expectedLogicalAxioms
                    )
            );
            Futures.addCallback(
                    future, new FutureCallback<Optional<KbDigest>>() {
                @Override
                public void onSuccess(Optional<KbDigest> result) {
                    setLastSyncedDigest(result);
                }

                @Override
                public void onFailure(Throwable t) {
                    logger.info(projectId, "There was an error when synchronizing the reasoner: ", t.getMessage());
                    setLastSyncedDigest(Optional.<KbDigest>absent());
                }
            }
            );
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


//        logger.info(projectId, "Checking to see if the reasoner needs synchronizing...");
//        ListenableFuture<GetKbDigestResponse> response = reasoningService.execute(new GetKbDigestAction(kbId));
//        Futures.addCallback(response, new FutureCallback<GetKbDigestResponse>() {
//            @Override
//            public void onSuccess(GetKbDigestResponse result) {
//                KbDigest reasonerDigest = result.getKbDigest();
//                synchronizeReasoner(reasonerDigest);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                logger.info(projectId, "There was a problem retrieving the reasoner digest: %s", t.getMessage());
//            }
//        });


//
//
//    private void synchronizeReasoner(KbDigest reasonerDigest) {
//        KbDigest expectedDigest = computedExpectedDigest();
//        logger.info(projectId,
//                    "Expected digest: %s    Reasoner digest: %s",
//                    expectedDigest, reasonerDigest);
//        if(expectedDigest.equals(reasonerDigest)) {
//            return;
//        }
//        if(Optional.of(reasonerDigest).equals(lastSyncedDigest)) {
//            flushChangesToReasoner();
//        }
//        else {
//            replaceAxiomsInReasoner();
//        }
//    }
//
//    private void replaceAxiomsInReasoner() {
//        logger.info(projectId, "Replacing axioms in reasoner");
//        currentChangeList.clear();
//        Set<? extends OWLAxiom> logicalAxioms = hasLogicalAxioms.getLogicalAxioms();
//        ImmutableList<OWLAxiom> axioms = ImmutableList.copyOf(logicalAxioms);
//        ListenableFuture<ReplaceAxiomsResponse> response = reasoningService.execute(new ReplaceAxiomsAction(kbId,
// axioms));
//
//        Futures.addCallback(response, new FutureCallback<ReplaceAxiomsResponse>() {
//            @Override
//            public void onSuccess(ReplaceAxiomsResponse result) {
//                lastSyncedDigest = Optional.of(result.getKbDigest());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                lastSyncedDigest = Optional.absent();
//                logger.info(projectId, "There was a problem replacing the axioms in the reasoner: %s",
// t.getMessage());
//            }
//        });
//
//    }
//
//    private void flushChangesToReasoner() {
//        if(currentChangeList.isEmpty()) {
//            return;
//        }
//        logger.info(projectId, "Flushing %d changes to reasoner", currentChangeList.size());
//        ImmutableList.Builder<AxiomChangeData> axiomChangeData = ImmutableList.builder();
//        for(OWLOntologyChange chg : currentChangeList) {
//            OWLOntologyChangeData changeData = chg.getChangeData();
//            if (changeData instanceof AxiomChangeData) {
//                axiomChangeData.add((AxiomChangeData) changeData);
//            }
//        }
//        currentChangeList.clear();
//        ListenableFuture<ApplyChangesResponse> response = reasoningService.execute(new ApplyChangesAction(kbId,
// axiomChangeData.build()));
//        Futures.addCallback(response, new FutureCallback<ApplyChangesResponse>() {
//            @Override
//            public void onSuccess(ApplyChangesResponse result) {
//                lastSyncedDigest = Optional.of(result.getKbDigest());
//                logger.info(projectId, "Reasoner synchronized.  Current reasoner digest: %s", result.getKbDigest());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                lastSyncedDigest = Optional.absent();
//                logger.info(projectId, "There was a problem synchronizing the reasoner: %s", t.getMessage());
//            }
//        });
//    }
//
//
//    private KbDigest computedExpectedDigest() {
//        TreeSet<OWLAxiom> logicalAxioms = Sets.newTreeSet();
//        logicalAxioms.addAll(hasLogicalAxioms.getLogicalAxioms());
//        return KbDigest.getDigest(logicalAxioms);
//    }


}
