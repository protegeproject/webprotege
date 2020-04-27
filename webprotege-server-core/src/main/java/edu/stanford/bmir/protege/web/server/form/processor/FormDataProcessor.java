package edu.stanford.bmir.protege.web.server.form.processor;

import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptorMissingException;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class FormDataProcessor {

    @Nonnull
    private final Provider<FormFrameBuilder> formFrameBuilderProvider;

    @Nonnull
    private final FormFieldProcessor formFieldProcessor;

    @Inject
    public FormDataProcessor(@Nonnull Provider<FormFrameBuilder> formFrameBuilderProvider,
                             @Nonnull FormFieldProcessor formFieldProcessor) {
        this.formFrameBuilderProvider = checkNotNull(formFrameBuilderProvider);
        this.formFieldProcessor = checkNotNull(formFieldProcessor);
    }

    public FormFrameBuilder processFormData(@Nonnull FormData formData,
                                            boolean subjectFactoryDescriptorRequired) {
        var formFrameBuilder = formFrameBuilderProvider.get();
        formData.getSubject()
                .ifPresent(formFrameBuilder::setSubject);
        if(subjectFactoryDescriptorRequired) {
            var formSubjectFactoryDescriptor = formData.getFormDescriptor()
                    .getSubjectFactoryDescriptor()
                    .orElseThrow(FormSubjectFactoryDescriptorMissingException::new);
            formFrameBuilder.setSubjectFactoryDescriptor(formSubjectFactoryDescriptor);
        }
        else {
            formData.getFormDescriptor()
                    .getSubjectFactoryDescriptor()
                    .ifPresent(formFrameBuilder::setSubjectFactoryDescriptor);
        }
        formData.getFormFieldData()
                .forEach(formFieldData -> formFieldProcessor.processFormFieldData(formFieldData, formFrameBuilder));
        return formFrameBuilder;
    }
}
