package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.reasoning.KbDigest;
import edu.stanford.protege.reasoning.KbId;
import edu.stanford.protege.reasoning.ReasoningService;
import edu.stanford.protege.reasoning.action.*;
import org.semanticweb.binaryowl.BinaryOWLVersion;
import org.semanticweb.binaryowl.owlobject.OWLObjectBinaryType;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public class ReasonerSynchronizationTask implements Callable<KbDigest> {

    private final WebProtegeLogger logger = WebProtegeLoggerManager.get(ReasonerSynchronizationTask.class);

    private final ProjectId projectId;

    private final KbId kbId;

    private final ReasoningService reasoningService;

    private final ImmutableSet<OWLLogicalAxiom> expectedLogicalAxioms;

    private final KbDigest expectedDigest;

    private final Optional<KbDigest> baseDigest;

    private final ImmutableList<AxiomChangeData> changesToApplyOnBaseDigest;


    public ReasonerSynchronizationTask(
            ProjectId projectId,
            ReasoningService reasoningService,
            Optional<KbDigest> baseDigest,
            ImmutableList<AxiomChangeData> changesToApplyOnBaseDigest,
            ImmutableSet<OWLLogicalAxiom> expectedLogicalAxioms) {
        this.projectId = projectId;
        this.kbId = new KbId(projectId.getId());
        this.reasoningService = reasoningService;
        this.expectedLogicalAxioms = expectedLogicalAxioms;
        this.baseDigest = baseDigest;
        this.changesToApplyOnBaseDigest = changesToApplyOnBaseDigest;

        TreeSet<OWLAxiom> sortedLogicalAxioms = Sets.newTreeSet();
        sortedLogicalAxioms.addAll(expectedLogicalAxioms);
        this.expectedDigest = KbDigest.getDigest(sortedLogicalAxioms);
    }


    @Override
    public KbDigest call() throws Exception {
        return synchronizeReasoner();
    }

    public KbDigest getExpectedDigest() {
        return expectedDigest;
    }

    /////////////////////////////////

    private KbDigest synchronizeReasoner() {
        try {
            logger.info(
                    projectId,
                    "Checking to see if the reasoner actually needs synchronizing.  The expected digest is %s.",
                    getExpectedDigest()
            );
            ListenableFuture<GetKbDigestResponse> future = reasoningService.execute(new GetKbDigestAction(kbId));
            GetKbDigestResponse response = future.get();
            return synchronizeReasoner(response.getKbDigest());
        } catch (InterruptedException e) {
            logInterruptedException();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            logger.info(projectId, "There was a problem getting the reasoner digest: " + cause.getMessage());
            throw new RuntimeException(e);
        }
    }

    private KbDigest synchronizeReasoner(final KbDigest reasonerDigest) {
        final KbDigest expectedDigest = getExpectedDigest();
        if (expectedDigest.equals(reasonerDigest)) {
            logger.info(
                    projectId,
                    "The reasoner is up to date.  The reasoner digest is %s.",
                    reasonerDigest
            );
            return expectedDigest;
        }
        logger.info(
                projectId,
                "The reasoner needs synchronizing.  Expected digest: %s    Reasoner digest: %s",
                expectedDigest,
                reasonerDigest
        );
        if (Optional.of(reasonerDigest).equals(baseDigest)) {
            if (!changesToApplyOnBaseDigest.isEmpty()) {
                return flushChangesToReasoner();
            }
            else {
                return expectedDigest;
            }
        }
        else {
            return replaceAxiomsInReasoner();
        }
    }

    private KbDigest replaceAxiomsInReasoner() {
        logger.info(projectId, "Replacing axioms in reasoner");
        Set<? extends OWLAxiom> logicalAxioms = expectedLogicalAxioms;
        ImmutableList<OWLAxiom> axioms = ImmutableList.copyOf(logicalAxioms);
        ListenableFuture<ReplaceAxiomsResponse> future = reasoningService.execute(
                new ReplaceAxiomsAction(
                        kbId, axioms
                )
        );
        try {
            ReplaceAxiomsResponse response = future.get();
            logger.info(projectId, "Axioms have been replaced.  The digest is now %s.", response.getKbDigest());
            checkSyncedDigest(response.getKbDigest());
            return response.getKbDigest();
        } catch (InterruptedException e) {
            logInterruptedException();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            logger.info(projectId, "There was a problem replacing the axioms in the reasoner: %s", cause.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void logInterruptedException() {
        logger.info(projectId, "Interrupted whilst getting reasoner digest");
    }

    private KbDigest flushChangesToReasoner() {
        logger.info(projectId, "Flushing %d changes to reasoner", changesToApplyOnBaseDigest.size());
        ListenableFuture<ApplyChangesResponse> future = reasoningService.execute(
                new ApplyChangesAction(
                        kbId, changesToApplyOnBaseDigest
                )
        );
        try {
            ApplyChangesResponse response = future.get();
            logger.info(projectId, "Changes have been flushed.  The digest is now %s.", response.getKbDigest());
            KbDigest syncedDigest = response.getKbDigest();
            checkSyncedDigest(syncedDigest);
            return syncedDigest;
        } catch (InterruptedException e) {
            logInterruptedException();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            logger.info(projectId, "There was a problem flushing changes to the reasoner: %s", cause.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void checkSyncedDigest(KbDigest syncedDigest) {
        if (!getExpectedDigest().equals(syncedDigest)) {
            logger.info(
                    projectId,
                    "WARNING! The expected digest is NOT THE SAME as the reasoner digest.  (Expected %s    Reasoner: %s)",
                    getExpectedDigest(),
                    syncedDigest
            );
        }
    }
}
