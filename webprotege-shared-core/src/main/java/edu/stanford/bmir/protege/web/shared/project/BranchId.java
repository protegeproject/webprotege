package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class BranchId {

    @Nonnull
    public static BranchId get(@Nonnull String id) {
        return get(id, false);
    }

    @JsonCreator
    @Nonnull
    public static BranchId get(@Nonnull String id, boolean isDefault) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new RuntimeException("BranchId is malformed.  BranchIds must be UUIDs");
        }
        return new AutoValue_BranchId(id, isDefault);
    }

    /**
     * Gets the string form of the {@link BranchId}.
     */
    @JsonValue
    @Nonnull
    public abstract String getId();

    @JsonValue
    @Nonnull
    public abstract boolean isDefault();

    @Nonnull
    public String toQuotedString() {
        return "\"" + getId() + "\"";
    }

    @GwtIncompatible
    public static BranchId generate() {
        return get(UUID.randomUUID().toString());
    }
}
