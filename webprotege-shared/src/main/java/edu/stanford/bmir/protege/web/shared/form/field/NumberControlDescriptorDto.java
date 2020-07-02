package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class NumberControlDescriptorDto implements FormControlDescriptorDto {

    @Nonnull
    public static NumberControlDescriptorDto get(@Nonnull NumberControlDescriptor descriptor) {
        return new AutoValue_NumberControlDescriptorDto(descriptor);
    }

    @Nonnull
    public abstract NumberControlDescriptor getDescriptor();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FormControlDescriptor toFormControlDescriptor() {
        return getDescriptor();
    }

    public String getFormat() {
        return getDescriptor().getFormat();
    }

    public NumberControlRange getRange() {
        return getDescriptor().getRange();
    }

    public LanguageMap getPlaceholder() {
        return getDescriptor().getPlaceholder();
    }

    public int getLength() {
        return getDescriptor().getLength();
    }
}
