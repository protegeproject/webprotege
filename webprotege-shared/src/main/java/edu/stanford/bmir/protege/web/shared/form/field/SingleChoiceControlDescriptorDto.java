package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class SingleChoiceControlDescriptorDto implements FormControlDescriptorDto {

    @Nonnull
    public static SingleChoiceControlDescriptorDto get(@Nonnull SingleChoiceControlType widgetType,
                                                       @Nonnull ImmutableList<ChoiceDescriptorDto> availableChoices,
                                                       @Nonnull ChoiceListSourceDescriptor choiceListSourceDescriptor) {
        return new AutoValue_SingleChoiceControlDescriptorDto(availableChoices, choiceListSourceDescriptor, widgetType);
    }

    @Nonnull
    public abstract ImmutableList<ChoiceDescriptorDto> getAvailableChoices();


    @Nonnull
    protected abstract ChoiceListSourceDescriptor getChoiceListSourceDescriptor();

    @Nonnull
    public abstract SingleChoiceControlType getWidgetType();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public SingleChoiceControlDescriptor toFormControlDescriptor() {
        return SingleChoiceControlDescriptor.get(getWidgetType(),
                                                 getChoiceListSourceDescriptor());
    }
}
