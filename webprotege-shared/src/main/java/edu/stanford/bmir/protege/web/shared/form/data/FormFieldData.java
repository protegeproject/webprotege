package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

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
                                    @Nonnull Page<FormControlData> formControlData) {
        return new AutoValue_FormFieldData(descriptor, formControlData);
    }

    @Nonnull
    public abstract FormFieldDescriptor getFormFieldDescriptor();

    /**
     * Gets the page of form control values for this field.
     */
    @Nonnull
    public abstract Page<FormControlData> getFormControlData();
}
