package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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

//    private final IssueRepository repository;

    @Inject
    public GetIssuesActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<ReadPermissionValidator> readPermissionValidatorValidatorFactory
                                  //IssueRepository repository
                                  ) {
        super(projectManager);
        this.readPermissionValidatorValidatorFactory = readPermissionValidatorValidatorFactory;
//        this.repository = repository;
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
        // TODO: Should work out the issue number
//        int issueNumber = repository.findOneByProjectIdOrderByNumberDesc(project.getProjectId())
//                .map(i -> i.getNumber())
//                .orElse(0) + 1;
//        ProjectId projectId = project.getProjectId();
//        long timestamp = System.currentTimeMillis();
//        Issue issue = Issue.builder(projectId, issueNumber, executionContext.getUserId(), timestamp)
//                .withTitle("My issue")
//                .withBody("This is a test issue. Ask @{Matthew Horridge} about it.  See Class(<http://ontology.com/classes/A>) as well.")
//                .assignTo(UserId.getUserId("MH"), executionContext.getUserId(), timestamp)
//                .addComment(new Comment(executionContext.getUserId(), timestamp, Optional.empty(), "This seems to work.  Check that it's o.k. with revision R33."), timestamp)
//                .addLabel("Question", executionContext.getUserId(), timestamp)
//                .milestone(new Milestone("Release 1.0"), executionContext.getUserId(), timestamp)
//                // TODO: With comments?
//                .build(new MentionParser());
//        // TODO: Reparse mentions
//        repository.save(issue);
//        System.out.println("Saved: " + issue);
//
//        List<Issue> issues = repository.findByProjectId(projectId)
//                .collect(toList());
//


        System.out.println("Got the issues!");

        return new GetIssuesResult(project.getProjectId(), new ArrayList<>());
    }
}
