package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class SingleChoiceControlDataDto implements FormControlDataDto {

    public SingleChoiceControlDataDto get(@Nonnull SingleChoiceControlDescriptor descriptor,
                                          @Nullable PrimitiveFormControlData choice) {
        return new AutoValue_SingleChoiceControlDataDto(descriptor, choice);
    }

    @Nonnull
    public abstract SingleChoiceControlDescriptor getDescriptor();

    @JsonProperty("choice")
    @Nullable
    protected abstract PrimitiveFormControlData getChoiceInternal();

    @Nonnull
    public Optional<PrimitiveFormControlData> getChoice() {
        return Optional.ofNullable(getChoiceInternal());
    }

}
