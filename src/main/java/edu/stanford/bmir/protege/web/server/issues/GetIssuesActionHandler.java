package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.issues.events.IssueAssigned;
import edu.stanford.bmir.protege.web.shared.issues.events.IssueLabelled;
import edu.stanford.bmir.protege.web.shared.issues.mention.UserIdMention;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.*;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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
        int issueNumber = (int)repository.count() + 1;
        long timestamp = System.currentTimeMillis();
        IssueRecord record = new IssueRecord(
                project.getProjectId(),
                issueNumber,
                executionContext.getUserId(),
                timestamp,
                "My new issue",
                "This is a test of the issue system",
                Status.OPEN,
                Optional.<UserId>empty(),
                Optional.of(new Milestone("Release 1.0")),
                ImmutableList.<String>of(),
                ImmutableList.of(
                        new Comment(
                                UserId.getGuest(),
                                timestamp,
                                Optional.<Long>empty(),
                                "The body of the comment",
                                ImmutableSet.of())
                ),
                ImmutableList.of(new UserIdMention(UserId.getUserId("Matty Horridge"))),
                ImmutableList.of(
                        new IssueAssigned(executionContext.getUserId(), timestamp, UserId.getUserId("Matthew Horridge")),
                        new IssueLabelled(executionContext.getUserId(), timestamp, "MyLovelyIssue")
                ));
        repository.save(record);

        List<Issue> issues = repository.findByProjectId(project.getProjectId())
                .map(ir -> ir.toIssue())
                .collect(toList());

        System.out.println("Got the issues!");

        return new GetIssuesResult(project.getProjectId(), issues);
    }
}
