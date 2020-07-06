package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptorDto;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class SubFormControlDescriptorDto implements FormControlDescriptorDto {

    @Nonnull
    public static SubFormControlDescriptorDto get(@Nonnull FormDescriptorDto subformDescriptorDto) {
        return new AutoValue_SubFormControlDescriptorDto(subformDescriptorDto);
    }

    @Nonnull
    public abstract FormDescriptorDto getDescriptor();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FormControlDescriptor toFormControlDescriptor() {
        return new SubFormControlDescriptor(getDescriptor().toFormDescriptor());
    }
}
