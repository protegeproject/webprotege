package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.issues.GetIssuesAction;
import edu.stanford.bmir.protege.web.shared.issues.GetIssuesResult;
import edu.stanford.bmir.protege.web.shared.issues.Status;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class GetIssuesActionHandler extends AbstractHasProjectActionHandler<GetIssuesAction, GetIssuesResult> {

    private final ValidatorFactory<ReadPermissionValidator> readPermissionValidatorValidatorFactory;

    private final IssueRecordRepository repository;

    @Inject
    public GetIssuesActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<ReadPermissionValidator> readPermissionValidatorValidatorFactory, IssueRecordRepository repository) {
        super(projectManager);
        this.readPermissionValidatorValidatorFactory = readPermissionValidatorValidatorFactory;
        this.repository = repository;
    }

    @Override
    public Class<GetIssuesAction> getActionClass() {
        return GetIssuesAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetIssuesAction action, RequestContext requestContext) {
        return readPermissionValidatorValidatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected GetIssuesResult execute(GetIssuesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        long issueNumber = repository.count() + 1;

        IssueRecord record = new IssueRecord(
                project.getProjectId(),
                issueNumber,
                "My new issue",
                "This is a test of the issue system",
                executionContext.getUserId(),
                System.currentTimeMillis(),
                0,
                0,
                Status.OPEN,
                Optional.<UserId>empty(),
                "",
                ImmutableList.<String>of(),
                ImmutableList.<OWLEntity>of());
        repository.save(record);

        repository.findByProjectIdAndTargetEntities(project.getProjectId(), new OWLClassImpl(IRI.create("http://stuff.com/A")))
                .forEach(System.out::println);

        Optional<IssueRecord> rec = repository.findByProjectIdAndNumber(project.getProjectId(), 6);
        rec.ifPresent(r -> {
            IssueRecord repl = new IssueRecord(r.getProjectId(),
                    r.getNumber(),
                    r.getTitle(),
                    r.getBody(),
                    r.getOwner(),
                    r.getCreatedAt(),
                    System.currentTimeMillis(),
                    0,
                    Status.OPEN,
                    Optional.of(executionContext.getUserId()), "", ImmutableList.<String>of(), ImmutableList.<OWLEntity>of(
                    new OWLClassImpl(IRI.create("http://stuff.com/A")),
                    new OWLObjectPropertyImpl(IRI.create("http://stuff.com/prop"))
            ));
            repository.deleteByProjectIdAndNumber(r.getProjectId(), r.getNumber());
            repository.save(repl);
        });

        return new GetIssuesResult();
    }
}
