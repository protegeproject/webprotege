package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormFieldId {

    @JsonCreator
    @Nonnull
    public static FormFieldId get(@Nonnull String id) {
        return new AutoValue_FormFieldId(id);
    }

    @JsonValue
    public abstract String getId();
}
