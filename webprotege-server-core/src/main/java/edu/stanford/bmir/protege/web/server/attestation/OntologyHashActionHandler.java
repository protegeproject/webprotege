package edu.stanford.bmir.protege.web.server.attestation;

import ch.unifr.digits.webprotege.attestation.server.ChangeTrackingAttestationService;
import ch.unifr.digits.webprotege.attestation.server.OntologyAttestationService;
import ch.unifr.digits.webprotege.attestation.shared.OntologyHashAction;
import ch.unifr.digits.webprotege.attestation.shared.OntologyHashResult;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.math.BigInteger;
import java.util.List;

public class OntologyHashActionHandler implements ProjectActionHandler<OntologyHashAction, OntologyHashResult> {

    @Nonnull
    private final RevisionManager revisionManager;
    private final ChangeTrackingAttestationService service = new ChangeTrackingAttestationService();

    @Inject
    public OntologyHashActionHandler(@Nonnull RevisionManager revisionManager) {
        this.revisionManager = revisionManager;
    }

    /**
     * Gets the class of {@link Action} handled by this handler.
     *
     * @return The class of {@link Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<OntologyHashAction> getActionClass() {
        return OntologyHashAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull OntologyHashAction action, @Nonnull RequestContext requestContext) {
        return new NullValidator();
    }

    @Nonnull
    @Override
    public OntologyHashResult execute(@Nonnull OntologyHashAction action, @Nonnull ExecutionContext executionContext) {
        RevisionNumber currentRevision = revisionManager.getCurrentRevision();
        OWLOntologyManager ontologyManager = revisionManager.getOntologyManagerForRevision(currentRevision);
        IRI iri = IRI.create(action.getIri());
        OWLOntology ontology = ontologyManager.getOntology(iri);
        String hash = service.ontologyHash(ontology);
        List<Integer> classHashes = service.classHashes(ontology);
        return new OntologyHashResult(hash, 0, classHashes);
    }
}
