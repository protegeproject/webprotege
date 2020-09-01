package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.WithProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.shared.node.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-28
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PerspectiveLayout {

    public static final String PERSPECTIVE_ID = "perspectiveId";

    public static final String LAYOUT = "layout";

    @Nonnull
    public static PerspectiveLayout get(@Nonnull PerspectiveId perspectiveId,
                                        @Nonnull Optional<Node> layout) {
        return getInternal(perspectiveId, layout.orElse(null));
    }
    @Nonnull
    public static PerspectiveLayout get(@Nonnull PerspectiveId perspectiveId,
                                        @Nonnull Node layout) {
        return getInternal(perspectiveId, layout);
    }

    @Nonnull
    public static PerspectiveLayout get(@Nonnull PerspectiveId perspectiveId) {
        return getInternal(perspectiveId, null);
    }

    @JsonCreator
    @Nonnull
    protected static PerspectiveLayout getInternal(@JsonProperty(PERSPECTIVE_ID) @Nullable PerspectiveId perspectiveId,
                                                   @JsonProperty(LAYOUT) @Nullable Node layout) {
        return new AutoValue_PerspectiveLayout(perspectiveId, layout);
    }

    @JsonProperty(PERSPECTIVE_ID)
    @Nonnull
    public abstract PerspectiveId getPerspectiveId();

    @JsonProperty(LAYOUT)
    @Nullable
    protected abstract Node getLayoutInternal();

    @JsonIgnore
    public Optional<Node> getLayout() {
        return Optional.ofNullable(getLayoutInternal());
    }
}
