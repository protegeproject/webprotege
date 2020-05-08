package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormFieldDataDto {

    @Nonnull
    public static FormFieldDataDto get(@Nonnull FormFieldDescriptor descriptor,
                                    @Nonnull Page<FormControlDataDto> formControlData) {
        return new AutoValue_FormFieldDataDto(descriptor, formControlData);
    }

    @Nonnull
    public abstract FormFieldDescriptor getFormFieldDescriptor();

    /**
     * Gets the page of form control values for this field.
     */
    @Nonnull
    public abstract Page<FormControlDataDto> getFormControlData();
}
