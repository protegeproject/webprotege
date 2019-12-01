package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormFieldData {

    @Nonnull
    public abstract String getType();

    @Nonnull
    public abstract Optional<FormDataValue> getValue();
}
