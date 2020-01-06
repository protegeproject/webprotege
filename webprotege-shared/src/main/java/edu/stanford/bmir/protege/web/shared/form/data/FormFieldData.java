package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormFieldData extends FormDataValue {

    @Nonnull
    public abstract FormFieldDescriptor getDescriptor();

    @Nonnull
    public abstract ImmutableList<FormControlData> getFormDataValue();
}
