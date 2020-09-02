package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static edu.stanford.bmir.protege.web.server.perspective.PerspectiveDescriptorsRecord.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
@AutoValue
@JsonPropertyOrder({PROJECT_ID, USER_ID, PERSPECTIVE_ID, LABEL, FAVORITE})
public abstract class PerspectiveDescriptorsRecord {

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    public static final String PERSPECTIVE_ID = "perspectiveId";

    public static final String LABEL = "label";

    public static final String FAVORITE = "favorite";

    @JsonCreator
    public static PerspectiveDescriptorsRecord get(@JsonProperty(PROJECT_ID) @Nullable ProjectId projectId,
                                                   @JsonProperty(USER_ID) @Nullable UserId userId,
                                                   @JsonProperty(PERSPECTIVE_ID) PerspectiveId perspectiveId,
                                                   @JsonProperty(LABEL) LanguageMap label,
                                                   @JsonProperty(FAVORITE) boolean favorite) {
        return new AutoValue_PerspectiveDescriptorsRecord(projectId, userId, perspectiveId, label, favorite);
    }

    @Nonnull
    public static PerspectiveDescriptorsRecord get(@Nonnull PerspectiveDescriptor descriptor) {
        return get(null, null, descriptor.getPerspectiveId(), descriptor.getLabel(), descriptor.isFavorite());
    }

    @Nonnull
    public static PerspectiveDescriptorsRecord get(@Nonnull ProjectId projectId,
                                                   @Nonnull PerspectiveDescriptor descriptor) {
        return get(projectId, null, descriptor.getPerspectiveId(), descriptor.getLabel(), descriptor.isFavorite());
    }

    @JsonProperty(PROJECT_ID)
    @Nullable
    public abstract ProjectId getProjectId();

    @JsonProperty(USER_ID)
    @Nullable
    public abstract UserId getUserId();

    @JsonProperty(PERSPECTIVE_ID)
    @Nonnull
    public abstract PerspectiveId getPerspectiveId();

    @JsonProperty(LABEL)
    @Nonnull
    public abstract LanguageMap getLabel();

    @JsonProperty(FAVORITE)
    public abstract boolean isFavorite();

    @JsonIgnore
    public PerspectiveDescriptor getDescriptor() {
        return PerspectiveDescriptor.get(getPerspectiveId(), getLabel(), isFavorite());
    }
}
