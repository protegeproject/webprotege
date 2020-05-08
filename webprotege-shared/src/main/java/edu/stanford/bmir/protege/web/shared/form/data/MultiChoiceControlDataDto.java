package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptor;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class MultiChoiceControlDataDto implements FormControlDataDto {

    @Nonnull
    public static MultiChoiceControlDataDto get(@Nonnull MultiChoiceControlDescriptor descriptor,
                                                @Nonnull ImmutableList<PrimitiveFormControlData> values) {
        return new AutoValue_MultiChoiceControlDataDto(descriptor, values);
    }

    @JsonProperty("descriptor")
    @Nonnull
    public abstract MultiChoiceControlDescriptor getDescriptor();

    @JsonProperty("values")
    @Nonnull
    public abstract ImmutableList<PrimitiveFormControlData> getValues();
}
