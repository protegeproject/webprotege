package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import edu.stanford.protege.reasoning.Action;
import edu.stanford.protege.reasoning.KbDigest;
import edu.stanford.protege.reasoning.Response;
import edu.stanford.protege.reasoning.action.ActionHandler;
import edu.stanford.protege.reasoning.action.ReasonerState;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 29/09/2014
 */
public interface ProjectReasoningService {

    Optional<KbDigest> getLastSyncedDigest();

    Optional<ReasonerState> getReasonerState(KbDigest digest);

    <A extends Action<R, ?>, R extends Response> ListenableFuture<R> executeQuery(final A action);

    void processOntologyChanges(List<OWLOntologyChange> changeList);
}
