package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.protege.widgetmap.shared.node.Node;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-31
 */
@AutoValue
public abstract class BuiltInPerspective {

    public static final PerspectiveId CLASSES_PERSPECTIVE_ID = PerspectiveId.get("69df8fa8-4f84-499e-9341-28eb5085c40b");

    public static final String PERSPECTIVE_ID = "perspectiveId";

    public static final String LABEL = "label";

    public static final String FAVORITE = "favorite";

    public static final String LAYOUT = "layout";


    @JsonCreator
    @Nonnull
    public static BuiltInPerspective get(@JsonProperty(PERSPECTIVE_ID) @Nonnull PerspectiveId perspectiveId,
                                         @JsonProperty(LABEL) @Nonnull LanguageMap label,
                                         @JsonProperty(FAVORITE) boolean favorite,
                                         @JsonProperty(LAYOUT) Node layout) {
        return new AutoValue_BuiltInPerspective(perspectiveId, label, favorite, layout);
    }

    @JsonProperty(PERSPECTIVE_ID)
    @Nonnull
    public abstract PerspectiveId getPerspectiveId();

    @JsonProperty(LABEL)
    @Nonnull
    public abstract LanguageMap getLabel();

    @JsonProperty(FAVORITE)
    public abstract boolean isFavorite();

    @JsonProperty(LAYOUT)
    @Nonnull
    public abstract Node getLayout();




}
