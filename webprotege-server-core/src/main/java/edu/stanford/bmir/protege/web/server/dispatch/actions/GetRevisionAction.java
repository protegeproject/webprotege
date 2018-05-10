package edu.stanford.bmir.protege.web.server.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2018
 *
 * Serverside action only
 */
public class GetRevisionAction implements ProjectAction<GetRevisionResult> {

    private final ProjectId projectId;

    private final RevisionNumber revisionNumber;

    public GetRevisionAction(@Nonnull ProjectId projectId,
                             @Nonnull RevisionNumber revisionNumber) {
        this.projectId = checkNotNull(projectId);
        this.revisionNumber = checkNotNull(revisionNumber);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }
}
