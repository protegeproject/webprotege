package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormFieldData implements IsSerializable {

    public static FormFieldData get(@Nonnull FormFieldDescriptor descriptor,
                                    @Nonnull ImmutableList<FormControlData> formControlData,
                                    int formControlDataCount) {
        return new AutoValue_FormFieldData(descriptor, formControlData, formControlDataCount);
    }

    @Nonnull
    public abstract FormFieldDescriptor getFormFieldDescriptor();

    /**
     * Gets the list of form control values for this field.  The way the list is interpreted
     * depends upon the type of control.
     */
    @Nonnull
    public abstract ImmutableList<FormControlData> getFormControlData();

    public abstract int getFormControlDataCount();
}
