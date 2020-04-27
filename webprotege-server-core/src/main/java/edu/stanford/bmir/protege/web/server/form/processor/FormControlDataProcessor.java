package edu.stanford.bmir.protege.web.server.form.processor;

import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class FormControlDataProcessor {

    @Nonnull
    private final GridControlDataProcessor gridControlDataProcessor;

    @Nonnull
    private final Provider<FormDataProcessor> formDataProcessorProvider;

    @Inject
    public FormControlDataProcessor(@Nonnull GridControlDataProcessor gridControlDataProcessor,
                                    @Nonnull Provider<FormDataProcessor> formDataProcessorProvider) {
        this.gridControlDataProcessor = gridControlDataProcessor;
        this.formDataProcessorProvider = formDataProcessorProvider;
    }

    public void processFormControlData(@Nonnull OwlBinding binding,
                                       @Nonnull FormControlData formControlData,
                                       @Nonnull FormFrameBuilder formFrameBuilder) {
        formControlData.accept(new FormControlDataVisitor() {
            @Override
            public void visit(@Nonnull EntityNameControlData entityNameControlData) {
                entityNameControlData.getEntity()
                                     .ifPresent(entity -> formFrameBuilder.addEntityValue(binding, entity));
            }

            @Override
            public void visit(@Nonnull FormData formData) {
                var formDataProcessor = formDataProcessorProvider.get();
                var nestedFormFrameBuilder = formDataProcessor.processFormData(formData, true);
                formFrameBuilder.add(binding, nestedFormFrameBuilder);
            }

            @Override
            public void visit(@Nonnull GridControlData gridControlData) {
                gridControlDataProcessor.processGridControlData(binding, gridControlData, formFrameBuilder);
            }

            @Override
            public void visit(@Nonnull ImageControlData imageControlData) {
                imageControlData.getIri()
                                .ifPresent(iri -> formFrameBuilder.addIriValue(binding, iri));
            }

            @Override
            public void visit(@Nonnull MultiChoiceControlData multiChoiceControlData) {
                multiChoiceControlData.getValues()
                                      .forEach(data -> processPrimitiveControlData(formFrameBuilder,
                                                                                   binding,
                                                                                   data));
            }

            @Override
            public void visit(@Nonnull SingleChoiceControlData singleChoiceControlData) {
                singleChoiceControlData.getChoice()
                                       .ifPresent(data -> processPrimitiveControlData(formFrameBuilder,
                                                                                      binding,
                                                                                      data));
            }

            @Override
            public void visit(@Nonnull NumberControlData numberControlData) {
                numberControlData.getValue()
                                 .ifPresent(value -> formFrameBuilder.addLiteralValue(binding, value));
            }

            @Override
            public void visit(@Nonnull TextControlData textControlData) {
                textControlData.getValue()
                               .ifPresent(literal ->
                                                  formFrameBuilder.addLiteralValue(binding, literal));
            }
        });
    }

    /**
     * Add the primitive value to the form frame build using the specified binding
     *
     * @param formFrameBuilder The form frame builder
     * @param binding          The binding that binds the primitive value to the subject of the form frame
     * @param value            The value
     */
    private void processPrimitiveControlData(@Nonnull FormFrameBuilder formFrameBuilder,
                                             @Nonnull OwlBinding binding,
                                             @Nonnull PrimitiveFormControlData value) {
        value.asEntity()
             .ifPresent(entity -> formFrameBuilder.addEntityValue(binding, entity));
        value.asLiteral()
             .ifPresent(literal -> formFrameBuilder.addLiteralValue(binding, literal));
        value.asIri()
             .ifPresent(iri -> formFrameBuilder.addIriValue(binding, iri));
    }
}
