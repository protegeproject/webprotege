package edu.stanford.bmir.protege.web.shared.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptorDto;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormDescriptorDto {

    public static FormDescriptorDto get(@Nonnull FormId formId,
                                        @Nonnull LanguageMap label,
                                        @Nonnull ImmutableList<FormFieldDescriptorDto> fields,
                                        @Nullable FormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        return new AutoValue_FormDescriptorDto(formId, label, fields, subjectFactoryDescriptor);
    }

    @Nonnull
    public abstract FormId getFormId();

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract ImmutableList<FormFieldDescriptorDto> getFields();

    @Nullable
    protected abstract FormSubjectFactoryDescriptor getFormSubjectFactoryDescriptorInternal();

    public Optional<FormSubjectFactoryDescriptor> getFormSubjectFactoryDescriptor() {
        return Optional.ofNullable(getFormSubjectFactoryDescriptorInternal());
    }

    @Nonnull
    public FormDescriptor toFormDescriptor() {
        return new FormDescriptor(getFormId(),
                                  getLabel(),
                                  getFields().stream().map(FormFieldDescriptorDto::toFormFieldDescriptor).collect(toImmutableList()),
                                  getFormSubjectFactoryDescriptor());
    }
}
