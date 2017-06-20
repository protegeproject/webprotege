package edu.stanford.bmir.protege.web.shared.perspective;


import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class SetPerspectivesAction implements ProjectAction<SetPerspectivesResult>, HasUserId, HasProjectId {

    private ProjectId projectId;

    private UserId userId;

    private ImmutableList<PerspectiveId> perspectiveIds;

    private SetPerspectivesAction() {
    }

    public SetPerspectivesAction(ProjectId projectId, UserId userId, ImmutableList<PerspectiveId> perspectiveIds) {
        this.projectId = projectId;
        this.userId = userId;
        this.perspectiveIds = perspectiveIds;
        GWT.log("[SetPerspectivesAction] " + perspectiveIds);
    }

    public ImmutableList<PerspectiveId> getPerspectiveIds() {
        return perspectiveIds;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public UserId getUserId() {
        return userId;
    }
}

