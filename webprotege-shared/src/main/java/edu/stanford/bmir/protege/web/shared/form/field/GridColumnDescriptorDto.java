package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class GridColumnDescriptorDto {


    @Nonnull
    public static GridColumnDescriptorDto get(@Nonnull GridColumnId columnId,
                                                 @Nonnull Optionality optionality,
                                                 @Nonnull Repeatability repeatability,
                                                 @Nullable OwlBinding binding,
                                                 @Nonnull LanguageMap label,
                                                 @Nonnull FormControlDescriptorDto formControlDescriptorDto) {
        return new AutoValue_GridColumnDescriptorDto(columnId,
                                                     optionality,
                                                     repeatability,
                                                     binding,
                                                     label,
                                                     formControlDescriptorDto);
    }


    @Nonnull
    public abstract GridColumnId getId();

    @Nonnull
    public abstract Optionality getOptionality();

    @Nonnull
    public abstract Repeatability getRepeatability();

    @JsonIgnore
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Nonnull
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract FormControlDescriptorDto getFormControlDescriptor();

    public GridColumnDescriptor toGridColumnDescriptor() {
        return GridColumnDescriptor.get(
                getId(),
                getOptionality(),
                getRepeatability(),
                getOwlBindingInternal(),
                getLabel(),
                getFormControlDescriptor().toFormControlDescriptor()
        );
    }

    @JsonIgnore
    public int getNestedColumnCount() {
        FormControlDescriptorDto formControlDescriptor = getFormControlDescriptor();
        if(formControlDescriptor instanceof GridControlDescriptorDto) {
            return ((GridControlDescriptorDto) getFormControlDescriptor()).getNestedColumnCount();
        }
        else {
            return 1;
        }
    }


    @JsonIgnore
    public Stream<GridColumnDescriptorDto> getLeafColumnDescriptors() {
        FormControlDescriptorDto formControlDescriptor = getFormControlDescriptor();
        if(formControlDescriptor instanceof GridControlDescriptorDto) {
            // This is not a leaf column
            return ((GridControlDescriptorDto) formControlDescriptor).getLeafColumns();
        }
        else {
            // This is a leaf column
            return Stream.of(this);
        }
    }


    @JsonIgnore
    public boolean isLeafColumnDescriptor() {
        return !(getFormControlDescriptor() instanceof GridControlDescriptorDto);
    }

}
