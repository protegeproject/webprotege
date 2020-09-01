package edu.stanford.bmir.protege.web.shared.perspective;


import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class SetPerspectivesAction implements ProjectAction<SetPerspectivesResult>, HasUserId, HasProjectId {

    private ProjectId projectId;

    private UserId userId;

    private ImmutableList<PerspectiveDescriptor> perspectiveIds;

    private SetPerspectivesAction() {
    }

    public SetPerspectivesAction(ProjectId projectId, UserId userId, ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        this.projectId = projectId;
        this.userId = userId;
        this.perspectiveIds = perspectiveIds;
    }

    @Nonnull
    public ImmutableList<PerspectiveDescriptor> getPerspectiveDescriptors() {
        return perspectiveIds;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public UserId getUserId() {
        return userId;
    }
}

