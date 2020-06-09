package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormFieldId implements FormRegionId {

    @JsonCreator
    @Nonnull
    public static FormFieldId get(@Nonnull String id) {
        checkFormat(id);
        return new AutoValue_FormFieldId(id);
    }

    private static void checkFormat(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new IllegalArgumentException("Malformed FormFieldId: " + id);
        }
    }

    @Override
    @JsonValue
    public abstract String getId();
}
