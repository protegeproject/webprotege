package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-13
 */
public class EntityFormDataConverter {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final Provider<FormFrameBuilder> formFrameBuilderProvider;

    @Nonnull
    private final Provider<FormSubjectResolver> formSubjectResolverProvider;

    @Inject
    public EntityFormDataConverter(@Nonnull OWLDataFactory dataFactory,
                                   @Nonnull RenderingManager renderingManager,
                                   @Nonnull Provider<FormFrameBuilder> formFrameBuilderProvider,
                                   @Nonnull Provider<FormSubjectResolver> formSubjectResolverProvider) {
        this.dataFactory = dataFactory;
        this.renderingManager = renderingManager;
        this.formFrameBuilderProvider = formFrameBuilderProvider;
        this.formSubjectResolverProvider = formSubjectResolverProvider;
    }

    /**
     * Converts the specified {@link FormData} into various
     * @param formData
     * @return
     */
    public FormFrame convert(@Nonnull FormData formData) {
        FormFrameBuilder formFrameBuilder = buildFormFrame(formData);
        var formSubjectResolver = formSubjectResolverProvider.get();
        return formFrameBuilder.build(formSubjectResolver);
    }

    public FormFrameBuilder buildFormFrame(@Nonnull FormData formData) {
        var formFrameBuilder = formFrameBuilderProvider.get();
        formData.getSubject()
                .ifPresent(formFrameBuilder::setSubject);
        formData.getFormDescriptor()
                .getSubjectFactoryDescriptor()
                .ifPresent(formFrameBuilder::setSubjectFactoryDescriptor);
        formData.getFormFieldData()
                .forEach(formFieldData -> processFormFieldData(formFieldData, formFrameBuilder));
        return formFrameBuilder;
    }


    private void processFormFieldData(FormFieldData formFieldData,
                                      FormFrameBuilder formFrameBuilder) {
        var formFieldDescriptor = formFieldData.getFormFieldDescriptor();
        var owlBinding = formFieldDescriptor.getOwlBinding();
        owlBinding.ifPresent(binding -> {
            var formControlData = formFieldData.getFormControlData();
            formControlData.forEach(fcd -> processFormControlData(binding, fcd, formFrameBuilder));

        });
    }

    private void processFormControlData(@Nonnull OwlBinding binding,
                                        @Nonnull FormControlData formControlData,
                                        @Nonnull FormFrameBuilder formFrameBuilder) {
        formControlData.accept(new FormControlDataVisitor() {
            @Override
            public void visit(@Nonnull EntityNameControlData entityNameControlData) {
                entityNameControlData.getEntity()
                                     .ifPresent(entity -> formFrameBuilder.add(binding, entity));
            }

            @Override
            public void visit(@Nonnull FormData formData) {
                var nestedFormFrameBuilder = buildFormFrame(formData);
                formFrameBuilder.add(binding, nestedFormFrameBuilder);
            }

            @Override
            public void visit(@Nonnull GridControlData gridControlData) {
                addGridControlData(binding, gridControlData, formFrameBuilder);
            }

            @Override
            public void visit(@Nonnull ImageControlData imageControlData) {
                imageControlData.getIri().ifPresent(iri -> formFrameBuilder.add(binding, iri));
            }

            @Override
            public void visit(@Nonnull MultiChoiceControlData multiChoiceControlData) {
                multiChoiceControlData.getValues()
                                      .forEach(primitiveFormControlData -> {
                                          addPrimitiveFormControlData(binding,
                                                                      primitiveFormControlData,
                                                                      formFrameBuilder);
                                      });
            }

            @Override
            public void visit(@Nonnull SingleChoiceControlData singleChoiceControlData) {
                singleChoiceControlData.getChoice().ifPresent(primitiveFormControlData -> {
                    addPrimitiveFormControlData(binding,
                                                primitiveFormControlData,
                                                formFrameBuilder);
                });
            }

            @Override
            public void visit(@Nonnull NumberControlData numberControlData) {
                numberControlData.getValue().ifPresent(value -> {
                    formFrameBuilder.add(binding,
                                                DataFactory.getOWLLiteral(Double.toString(value),
                                                                          DataFactory.getXSDDecimal()));
                });
            }

            @Override
            public void visit(@Nonnull TextControlData textControlData) {
                textControlData.getValue().ifPresent(literal ->
                        formFrameBuilder.add(binding, literal));
            }
        });
    }

    public void addGridControlData(@Nonnull OwlBinding binding,
                                   @Nonnull GridControlData gridControlData,
                                   @Nonnull FormFrameBuilder formFrameBuilder) {

        for(GridRowData gridRowData : gridControlData.getRows()) {
            var rowFrameBuilder = formFrameBuilderProvider.get();

            var rowSubject = gridRowData.getSubject();
            rowSubject.ifPresent(rowFrameBuilder::setSubject);

            var gridControlDescriptor = gridControlData.getDescriptor();
            var columnDescriptors = gridControlDescriptor
                                                   .getColumns();
            var rowCells = gridRowData.getCells();
            for(int i = 0; i < columnDescriptors.size(); i++) {
                var columnDescriptor = columnDescriptors.get(i);
                var gridCellData = rowCells.get(i);
                var cellBinding = columnDescriptor.getOwlBinding();
                cellBinding.ifPresent(theCellBinding -> {
                    gridCellData.getValue()
                                .ifPresent(cellControlData -> {
                                    processFormControlData(theCellBinding,
                                                           cellControlData,
                                                           rowFrameBuilder);
                                });
                });
            }
            gridControlDescriptor
                           .getSubjectFactoryDescriptor()
                           .ifPresent(rowFrameBuilder::setSubjectFactoryDescriptor);
            formFrameBuilder.add(binding, rowFrameBuilder);
        }
    }

    public void addPrimitiveFormControlData(OwlBinding binding,
                                            PrimitiveFormControlData primitiveFormControlData,
                                            FormFrameBuilder formFrameBuilder) {
        primitiveFormControlData.asEntity().ifPresent(entity -> formFrameBuilder.add(binding, entity));
        primitiveFormControlData.asLiteral().ifPresent(literal -> formFrameBuilder.add(binding, literal));
        primitiveFormControlData.asIri().ifPresent(iri -> formFrameBuilder.add(binding, iri));
    }


}
