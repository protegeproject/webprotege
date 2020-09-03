package edu.stanford.bmir.protege.web.shared.perspective;


import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class SetPerspectivesAction implements ProjectAction<SetPerspectivesResult>, HasProjectId {

    private ProjectId projectId;

    private UserId userId;

    private ImmutableList<PerspectiveDescriptor> perspectiveIds;

    private SetPerspectivesAction() {
    }

    public SetPerspectivesAction(@Nonnull ProjectId projectId,
                                 @Nonnull UserId userId,
                                 @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.perspectiveIds = checkNotNull(perspectiveIds);
    }

    /**
     * Set the perspectives for the project.  The ability to edit project settings is required for this.
     * @param projectId The project
     * @param perspectiveIds The perspectives
     */
    public SetPerspectivesAction(@Nonnull ProjectId projectId,
                                 @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        this.projectId = checkNotNull(projectId);
        this.userId = null;
        this.perspectiveIds = checkNotNull(perspectiveIds);
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

    public Optional<UserId> getUserId() {
        return Optional.ofNullable(userId);
    }
}

