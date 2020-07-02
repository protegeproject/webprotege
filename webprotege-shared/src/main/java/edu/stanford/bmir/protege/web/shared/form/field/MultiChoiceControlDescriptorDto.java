package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.data.MultiChoiceControlDataDto;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class MultiChoiceControlDescriptorDto implements FormControlDescriptorDto {

    @Nonnull
    public static MultiChoiceControlDescriptorDto get(@Nonnull ChoiceListSourceDescriptor choiceListSourceDescriptor,
                                                      @Nonnull ImmutableList<ChoiceDescriptorDto> choices) {
        return new AutoValue_MultiChoiceControlDescriptorDto(choiceListSourceDescriptor, choices);
    }

    @Nonnull
    public abstract ChoiceListSourceDescriptor getChoiceListSourceDescriptor();

    @Nonnull
    public abstract ImmutableList<ChoiceDescriptorDto> getAvailableChoices();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public MultiChoiceControlDescriptor toFormControlDescriptor() {
        return MultiChoiceControlDescriptor.get(
            getChoiceListSourceDescriptor(),
            ImmutableList.of()
        );
    }
}
