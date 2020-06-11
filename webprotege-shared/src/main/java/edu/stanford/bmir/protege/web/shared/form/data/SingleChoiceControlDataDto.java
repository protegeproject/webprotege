package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class SingleChoiceControlDataDto implements FormControlDataDto {

    @Nonnull
    public static SingleChoiceControlDataDto get(@Nonnull SingleChoiceControlDescriptor descriptor,
                                                 @Nullable PrimitiveFormControlDataDto choice,
                                                 int depth) {
        return new AutoValue_SingleChoiceControlDataDto(depth, descriptor, choice);
    }

    @Nonnull
    public abstract SingleChoiceControlDescriptor getDescriptor();

    @JsonProperty("choice")
    @Nullable
    protected abstract PrimitiveFormControlDataDto getChoiceInternal();

    @Nonnull
    public Optional<PrimitiveFormControlDataDto> getChoice() {
        return Optional.ofNullable(getChoiceInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public SingleChoiceControlData toFormControlData() {
        return SingleChoiceControlData.get(getDescriptor(),
                getChoice().map(PrimitiveFormControlDataDto::toPrimitiveFormControlData).orElse(null));
    }
}
