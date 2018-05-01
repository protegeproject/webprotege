package edu.stanford.bmir.protege.web.server.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 */
public class GetRevisionsAction implements ProjectAction<GetRevisionsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RevisionNumber from;

    @Nonnull
    private final RevisionNumber to;

    public GetRevisionsAction(@Nonnull ProjectId projectId, @Nonnull RevisionNumber from, @Nonnull RevisionNumber to) {
        this.projectId = checkNotNull(projectId);
        this.from = checkNotNull(from);
        this.to = checkNotNull(to);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public RevisionNumber getFrom() {
        return from;
    }

    @Nonnull
    public RevisionNumber getTo() {
        return to;
    }
}
