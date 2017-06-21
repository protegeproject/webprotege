package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.issues.GetIssuesAction;
import edu.stanford.bmir.protege.web.shared.issues.GetIssuesResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class GetIssuesActionHandler extends AbstractHasProjectActionHandler<GetIssuesAction, GetIssuesResult> {

//    private final IssueRepository repository;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public GetIssuesActionHandler(@Nonnull AccessManager accessManager,
                                  @Nonnull ProjectId projectId) {
        super(accessManager);
        this.projectId = projectId;
    }

    @Override
    public Class<GetIssuesAction> getActionClass() {
        return GetIssuesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_ANY_ISSUE;
    }

    @Override
    public GetIssuesResult execute(GetIssuesAction action, ExecutionContext executionContext) {
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


        return new GetIssuesResult(projectId, new ArrayList<>());
    }
}
