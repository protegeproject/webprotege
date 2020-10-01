package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
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
@JsonPropertyOrder({PROJECT_ID, USER_ID, PERSPECTIVES})
public abstract class PerspectiveDescriptorsRecord {

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    public static final String PERSPECTIVES = "perspectives";

    @JsonCreator
    public static PerspectiveDescriptorsRecord get(@JsonProperty(PROJECT_ID) @Nullable ProjectId projectId,
                                                   @JsonProperty(USER_ID) @Nullable UserId userId,
                                                   @JsonProperty(PERSPECTIVES) ImmutableList<PerspectiveDescriptor> perspectives) {
        return new AutoValue_PerspectiveDescriptorsRecord(projectId, userId, perspectives);
    }

    @Nonnull
    public static PerspectiveDescriptorsRecord get(@Nonnull ImmutableList<PerspectiveDescriptor> perspectives) {
        return get(null, null, perspectives);
    }

    @Nonnull
    public static PerspectiveDescriptorsRecord get(@Nonnull ProjectId projectId,
                                                   @Nonnull ImmutableList<PerspectiveDescriptor> perspectives) {
        return get(projectId, null, perspectives);
    }

    @JsonProperty(PROJECT_ID)
    @Nullable
    public abstract ProjectId getProjectId();

    @JsonProperty(USER_ID)
    @Nullable
    public abstract UserId getUserId();

    @JsonProperty(PERSPECTIVES)
    @Nonnull
    public abstract ImmutableList<PerspectiveDescriptor> getPerspectives();
}
