package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.shared.node.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
@AutoValue
public abstract class PerspectiveLayoutRecord {

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    public static final String PERSPECTIVE_ID = "perspectiveId";

    public static final String LAYOUT = "layout";


    @JsonCreator
    @Nonnull
    protected static PerspectiveLayoutRecord get(@JsonProperty(PROJECT_ID) @Nullable ProjectId projectId,
                                               @JsonProperty(USER_ID) @Nullable UserId userId,
                                               @JsonProperty(PERSPECTIVE_ID) @Nonnull PerspectiveId perspectiveId,
                                               @JsonProperty(LAYOUT) @Nullable Node layout) {
        return new AutoValue_PerspectiveLayoutRecord(projectId, userId, perspectiveId, layout);
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

    @JsonProperty(LAYOUT)
    @Nullable
    public abstract Node getLayout();

    @Nonnull
    public PerspectiveLayout toPerspectiveLayout() {
        Node layout = getLayout();
        if (layout != null) {
            return PerspectiveLayout.get(getPerspectiveId(), layout);
        }
        else {
            return PerspectiveLayout.get(getPerspectiveId());
        }
    }

}
