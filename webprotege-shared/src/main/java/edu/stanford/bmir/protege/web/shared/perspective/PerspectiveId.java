package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PerspectiveId implements IsSerializable, Serializable {

    @JsonCreator
    @Nonnull
    public static PerspectiveId get(@Nonnull String id) {
        return new AutoValue_PerspectiveId(id);
    }

    /**
     * Gets the identifier for this perspective.  This is a human readable name.
     */
    @JsonValue
    @Nonnull
    public abstract String getId();
}
