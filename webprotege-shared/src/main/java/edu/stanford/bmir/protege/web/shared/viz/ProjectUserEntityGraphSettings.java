package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ProjectUserEntityGraphSettings implements HasProjectId, IsSerializable {

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    private static final String SETTINGS = "settings";

    @JsonCreator
    public static ProjectUserEntityGraphSettings get(@Nonnull @JsonProperty(PROJECT_ID) ProjectId projectId,
                                                     @Nullable @JsonProperty(USER_ID) UserId userId,
                                                     @Nonnull @JsonProperty(SETTINGS) EntityGraphSettings settings) {
        return new AutoValue_ProjectUserEntityGraphSettings(projectId, userId, settings);
    }

    @Nonnull
    public static ProjectUserEntityGraphSettings getDefault(@Nonnull ProjectId projectId,
                                                            @Nullable UserId userId) {
        return get(projectId, userId, EntityGraphSettings.getDefault());
    }

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty(USER_ID)
    @Nullable
    public abstract UserId getUserIdInternal();

    @JsonProperty(SETTINGS)
    @Nonnull
    public abstract EntityGraphSettings getSettings();

    @JsonIgnore
    @Nullable
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(getUserIdInternal());
    }
}
