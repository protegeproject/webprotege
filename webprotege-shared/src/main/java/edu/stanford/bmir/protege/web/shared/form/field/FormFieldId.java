package edu.stanford.bmir.protege.web.shared.form.field;

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
 * 30/03/16
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormFieldId implements FormRegionId {

    // Temporary normalization
    @JsonCreator
    @GwtIncompatible
    public static FormFieldId normalized(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            return get(UUID.randomUUID().toString());
        }
        else {
            return get(id);
        }
    }

    @Nonnull
    public static FormFieldId get(@Nonnull String id) {
        checkFormat(id);
        return new AutoValue_FormFieldId(id);
    }

    private static void checkFormat(String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new RuntimeException("FormFieldId is malformed: " + id);
        }
    }

    @Override
    @JsonValue
    public abstract String getId();
}
