package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.WithProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-31
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PerspectiveCoordinates implements WithProjectId<PerspectiveCoordinates> {

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    public static final String PERSPECTIVE_ID = "perspectiveId";

    @Nonnull
    public static PerspectiveCoordinates get(@Nonnull ProjectId projectId, @Nonnull UserId userId, @Nonnull PerspectiveId perspectiveId) {
        return getInternal(projectId, userId, perspectiveId);
    }

    @Nonnull
    public static PerspectiveCoordinates get(@Nonnull ProjectId projectId, @Nonnull PerspectiveId perspectiveId) {
        return getInternal(projectId, null, perspectiveId);
    }

    @Nonnull
    public static PerspectiveCoordinates get(@Nonnull PerspectiveId perspectiveId) {
        return getInternal(null, null, perspectiveId);
    }

    @Nonnull
    protected static PerspectiveCoordinates getInternal(@JsonProperty(PROJECT_ID) @Nullable ProjectId projectId,
                                                 @JsonProperty(USER_ID) @Nullable UserId userId,
                                                 @JsonProperty(PERSPECTIVE_ID) @Nullable PerspectiveId perspectiveId) {
        return new AutoValue_PerspectiveCoordinates(projectId, userId, perspectiveId);
    }

    @Override
    public PerspectiveCoordinates withProjectId(@Nonnull ProjectId projectId) {
        if(getProjectId().isPresent() && getProjectId().get().equals(projectId)) {
            return this;
        }
        return getInternal(getProjectIdInternal(),
                           getUserIdInternal(),
                           getPerspectiveId());
    }

    @JsonProperty(PROJECT_ID)
    @Nullable
    protected abstract ProjectId getProjectIdInternal();


    @JsonIgnore
    @Nonnull
    public Optional<ProjectId> getProjectId() {
        return Optional.ofNullable(getProjectIdInternal());
    }

    @JsonProperty(PROJECT_ID)
    @Nullable
    protected abstract UserId getUserIdInternal();

    @JsonIgnore
    @Nonnull
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(getUserIdInternal());
    }

    @JsonProperty(PERSPECTIVE_ID)
    @Nonnull
    public abstract PerspectiveId getPerspectiveId();


}
