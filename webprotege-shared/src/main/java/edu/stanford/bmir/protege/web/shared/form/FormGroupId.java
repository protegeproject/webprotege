package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * 2020-04-27
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormGroupId {

    @JsonCreator
    public static FormGroupId get(@Nonnull String id) {
        checkFormat(id);
        return new AutoValue_FormGroupId(id);
    }

    @GwtIncompatible
    public static FormGroupId generate() {
        return get(UUID.randomUUID().toString());
    }

    @JsonValue
    @Nonnull
    public abstract String getId();

    public static void checkFormat(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new RuntimeException("Malformed FormGroupId.  FormGroupIds should be UUIDs");
        }
    }
}
