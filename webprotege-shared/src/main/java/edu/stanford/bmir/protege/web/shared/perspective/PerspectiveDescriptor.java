package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-31
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PerspectiveDescriptor {

    public static final String PERSPECTIVE_ID = "perspectiveId";

    public static final String LABEL = "label";

    public static final String FAVORITE = "favorite";

    @JsonCreator
    @Nonnull
    public static PerspectiveDescriptor get(@JsonProperty(PERSPECTIVE_ID) @Nonnull PerspectiveId perspectiveId,
                                            @JsonProperty(LABEL) @Nonnull LanguageMap newLabel,
                                            @JsonProperty(FAVORITE) boolean favorite) {
        return new AutoValue_PerspectiveDescriptor(perspectiveId, newLabel, favorite);
    }


    @JsonProperty(PERSPECTIVE_ID)
    @Nonnull
    public abstract PerspectiveId getPerspectiveId();

    @JsonProperty(LABEL)
    @Nonnull
    public abstract LanguageMap getLabel();

    @JsonProperty(FAVORITE)
    public abstract boolean isFavorite();

    @Nonnull
    public PerspectiveDescriptor withFavorite(boolean favorite) {
        if(favorite == isFavorite()) {
            return this;
        }
        return PerspectiveDescriptor.get(getPerspectiveId(), getLabel(), favorite);
    }
}
