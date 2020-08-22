package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormId implements Serializable, IsSerializable {

    @JsonCreator
    public static FormId get(@Nonnull String id) {
        checkFormat(id);
        return new AutoValue_FormId(id);
    }

    @Nonnull
    public static FormId valueOf(@Nonnull String id) {
        return get(id);
    }

    @GwtIncompatible
    public static FormId generate() {
        return get(UUID.randomUUID().toString());
    }


    public static void checkFormat(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new RuntimeException("Malformed Form Id.  Form Ids should be UUIDs");
        }
    }

    @JsonValue
    public abstract String getId();
}
