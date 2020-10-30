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

    @JsonCreator
    @Nonnull
    public static BranchId get(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new RuntimeException("BranchId is malformed.  BranchIds must be UUIDs");
        }
        return new AutoValue_BranchId(id);
    }

    /**
     * Gets the string form of the {@link BranchId}.
     */
    @JsonValue
    @Nonnull
    public abstract String getId();

    @Nonnull
    public String toQuotedString() {
        return "\"" + getId() + "\"";
    }

    @GwtIncompatible
    public static BranchId generate() {
        return get(UUID.randomUUID().toString());
    }
}
