package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormDataDto implements FormControlDataDto {

    @Nonnull
    public static FormDataDto get(@Nonnull FormSubjectDto subject,
                                  @Nonnull FormDescriptorDto formDescriptor,
                                  @Nonnull ImmutableList<FormFieldDataDto> formFieldData,
                                  int depth) {
        return new AutoValue_FormDataDto(depth, subject, formDescriptor, formFieldData);
    }

    @Nonnull
    public Optional<FormSubjectDto> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @JsonIgnore
    @Nullable
    protected abstract FormSubjectDto getSubjectInternal();

    public abstract FormDescriptorDto getFormDescriptor();

    public abstract ImmutableList<FormFieldDataDto> getFormFieldData();

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public FormControlData toFormControlData() {
        return FormData.get(getSubject().map(FormSubjectDto::toFormSubject),
                getFormDescriptor().toFormDescriptor(),
                getFormFieldData().stream().map(FormFieldDataDto::getFormFieldData).collect(toImmutableList()));
    }

    @Nonnull
    public FormData toFormData() {
        return FormData.get(getSubject().map(FormSubjectDto::toFormSubject),
                getFormDescriptor().toFormDescriptor(),
                getFormFieldData().stream().map(FormFieldDataDto::toFormFieldData).collect(toImmutableList()));
    }

    @Nonnull
    public FormId getFormId() {
        return getFormDescriptor().getFormId();
    }
}
