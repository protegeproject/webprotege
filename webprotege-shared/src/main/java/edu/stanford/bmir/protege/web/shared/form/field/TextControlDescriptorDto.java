package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class TextControlDescriptorDto implements FormControlDescriptorDto {

    public static TextControlDescriptorDto get(@Nonnull TextControlDescriptor descriptor) {
        return new AutoValue_TextControlDescriptorDto(descriptor);
    }

    @Nonnull
    public abstract TextControlDescriptor getDescriptor();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FormControlDescriptor toFormControlDescriptor() {
        return getDescriptor();
    }
}
