package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class GetIssuesResult implements Result, HasProjectId {

    private ProjectId projectId;

    private List<Issue> issues;

    private GetIssuesResult() {
    }

    public GetIssuesResult(ProjectId projectId, List<Issue> issues) {
        this.projectId = projectId;
        this.issues = new ArrayList<>(issues);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public List<Issue> getIssues() {
        return new ArrayList<>(issues);
    }
}
