package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormElementData extends FormDataValue {

    @Nonnull
    public abstract FormElementDescriptor getDescriptor();

    @Nonnull
    public abstract ImmutableList<FormFieldData> getFormDataValue();
}
