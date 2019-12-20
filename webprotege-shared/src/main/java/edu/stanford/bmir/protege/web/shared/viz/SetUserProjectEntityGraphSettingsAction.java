package edu.stanford.bmir.protege.web.shared.viz;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
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
 * 2019-12-10
 */
public class SetUserProjectEntityGraphSettingsAction implements ProjectAction<SetUserProjectEntityGraphResult> {

    @Nullable
    private UserId userId;

    private ProjectId projectId;

    private EntityGraphSettings settings;

    public SetUserProjectEntityGraphSettingsAction(@Nonnull ProjectId projectId,
                                                   @Nullable UserId userId,
                                                   @Nonnull EntityGraphSettings settings) {
        this.projectId = checkNotNull(projectId);
        this.settings = checkNotNull(settings);
        this.userId = userId;
    }

    @GwtSerializationConstructor
    private SetUserProjectEntityGraphSettingsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public EntityGraphSettings getSettings() {
        return settings;
    }

    @Nonnull
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(userId);
    }
}
