package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class ImageControlDescriptorDto implements FormControlDescriptorDto {

    @Nonnull
    public static ImageControlDescriptorDto get() {
        return new AutoValue_ImageControlDescriptorDto();
    }

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public ImageControlDescriptor toFormControlDescriptor() {
        return new ImageControlDescriptor();
    }
}
