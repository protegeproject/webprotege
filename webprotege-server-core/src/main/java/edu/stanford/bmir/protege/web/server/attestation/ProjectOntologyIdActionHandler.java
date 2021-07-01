package edu.stanford.bmir.protege.web.server.attestation;

import ch.unifr.digits.webprotege.attestation.shared.GetProjectOntologyIdAction;
import ch.unifr.digits.webprotege.attestation.shared.GetProjectOntologyIdResult;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectOntologyIdActionHandler implements ProjectActionHandler<GetProjectOntologyIdAction, GetProjectOntologyIdResult> {

    @Nonnull
    private final RevisionManager revisionManager;

    @Inject
    public ProjectOntologyIdActionHandler(@Nonnull RevisionManager revisionManager) {
        this.revisionManager = revisionManager;
    }

    @Nonnull
    @Override
    public Class<GetProjectOntologyIdAction> getActionClass() {
        return GetProjectOntologyIdAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetProjectOntologyIdAction action, @Nonnull RequestContext requestContext) {
        return new NullValidator();
    }

    @Nonnull
    @Override
    public GetProjectOntologyIdResult execute(@Nonnull GetProjectOntologyIdAction action, @Nonnull ExecutionContext executionContext) {
        RevisionNumber currentRevision = revisionManager.getCurrentRevision();
        OWLOntologyManager manager = revisionManager.getOntologyManagerForRevision(currentRevision);
        Set<OWLOntologyID> ids = manager.getOntologies().stream().map(OWLOntology::getOntologyID).collect(Collectors.toSet());
        return new GetProjectOntologyIdResult(ids);
    }
}
