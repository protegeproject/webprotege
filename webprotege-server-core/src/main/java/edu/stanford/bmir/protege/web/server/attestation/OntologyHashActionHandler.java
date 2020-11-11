package edu.stanford.bmir.protege.web.server.attestation;

import ch.unifr.digits.webprotege.attestation.server.AttestationService;
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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OntologyHashActionHandler implements ProjectActionHandler<OntologyHashAction, OntologyHashResult> {

    @Nonnull
    private final RevisionManager revisionManager;

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
        int hash = AttestationService.ontologyHash(ontology);
        Set<OWLClass> classesInSignature = ontology.getClassesInSignature();
        List<Integer> classHashes = classesInSignature.stream().map(AttestationService::entityHash).collect(Collectors.toList());
        return new OntologyHashResult(hash, classHashes);
    }
}
