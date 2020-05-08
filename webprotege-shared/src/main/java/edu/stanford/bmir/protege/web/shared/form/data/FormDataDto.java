package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormDataDto implements FormControlDataDto {

    @Nonnull
    public static FormDataDto get(@Nonnull FormSubjectDto subject,
                                  @Nonnull FormDescriptor formDescriptor,
                                  @Nonnull ImmutableList<FormFieldDataDto> formFieldData) {
        return new AutoValue_FormDataDto(subject, formDescriptor, formFieldData);
    }

    @Nonnull
    public Optional<FormSubjectDto> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @JsonIgnore
    @Nullable
    protected abstract FormSubjectDto getSubjectInternal();

    public abstract FormDescriptor getFormDescriptor();

    public abstract ImmutableList<FormFieldDataDto> getFormFieldData();
}
