package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.OWLPrimitive2FormControlDataConverter;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
public class EntityFrameFormDataDtoBuilder {


    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final FrameComponentSessionRenderer sessionRenderer;

    @Nonnull
    private final ChoiceDescriptorDtoSupplier choiceDescriptorDtoSupplier;

    @Nonnull
    private final PrimitiveFormControlDataDtoRenderer primitiveDataRenderer;

    @AutoFactory
    @Inject
    public EntityFrameFormDataDtoBuilder(@Provided @Nonnull OWLPrimitive2FormControlDataConverter converter,
                                         @Provided @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                                         @Provided @Nonnull BindingValuesExtractor bindingValuesExtractor,
                                         @Nonnull FrameComponentSessionRenderer sessionRenderer,
                                         @Provided @Nonnull ChoiceDescriptorDtoSupplier choiceDescriptorDtoSupplier,
                                         @Provided @Nonnull PrimitiveFormControlDataDtoRenderer primitiveDataRenderer) {
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        this.bindingValuesExtractor = bindingValuesExtractor;
        this.sessionRenderer = sessionRenderer;
        this.choiceDescriptorDtoSupplier = choiceDescriptorDtoSupplier;
        this.primitiveDataRenderer = primitiveDataRenderer;
    }

    private FormSubjectDto getFormSubject(OWLPrimitiveData root) {
        if(root instanceof IRIData) {
            return FormSubjectDto.get((IRIData) root);
        }
        else if(root instanceof OWLEntityData) {
            return FormSubjectDto.get((OWLEntityData) root);
        }
        else {
            throw new RuntimeException("Cannot process form subjects that are not IRIs or Entities");
        }
    }

    @Nullable
    private OWLEntityData toEntityFormSubject(OWLPrimitive primitive) {
        if(primitive instanceof OWLEntity) {
            return getRendering((OWLEntity) primitive);
        }
        else if(primitive instanceof IRI) {
            var iri = (IRI) primitive;
            return sessionRenderer.getRendering(iri).stream().sorted()
                                                       .findFirst()
                                                       .orElse(null);
        }
        else {
            return null;
        }
    }

    @Nonnull
    private OWLEntityData getRendering(@Nonnull OWLEntity entity) {
        return sessionRenderer.getEntityRendering(entity);
    }

    private ImmutableList<FormControlDataDto> toFormControlValues(@Nonnull OWLEntityData subject,
                                                               @Nonnull FormRegionId formFieldId,
                                                               @Nonnull BoundControlDescriptor descriptor,
                                                               @Nonnull FormPageRequestIndex formPageRequestIndex,
                                                               @Nonnull LangTagFilter langTagFilter) {
        var owlBinding = descriptor.getOwlBinding();
        if(owlBinding.isEmpty()) {
            return ImmutableList.of();
        }
        var theBinding = owlBinding.get();
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);

        var formControlDescriptor = descriptor.getFormControlDescriptor();
        return formControlDescriptor.accept(new FormControlDescriptorVisitor<>() {
            @Override
            public ImmutableList<FormControlDataDto> visit(TextControlDescriptor textControlDescriptor) {
                return values.stream()
                             .filter(p -> p instanceof OWLLiteral)
                             .map(p -> (OWLLiteral) p)
                             .map(literal -> TextControlDataDto.get(textControlDescriptor, literal))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(NumberControlDescriptor numberControlDescriptor) {
                return values.stream()
                             .filter(p -> p instanceof OWLLiteral)
                             .map(p -> (OWLLiteral) p)
                             .map(value -> NumberControlDataDto.get(numberControlDescriptor, value))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                var choiceSource = singleChoiceControlDescriptor.getSource();
                return values.stream()
                             .map(p -> primitiveDataRenderer.toFormControlDataDto(p, sessionRenderer))
                             .map(value -> SingleChoiceControlDataDto.get(singleChoiceControlDescriptor,
                                                                          toChoices(choiceSource),
                                                                          value))
                             .filter(data -> isIncluded(data, langTagFilter))
                             .limit(1)
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                var vals = values.stream()
                                 .map(p -> primitiveDataRenderer.toFormControlDataDto(p, sessionRenderer))
                                 .collect(toImmutableList());
                return ImmutableList.of(MultiChoiceControlDataDto.get(multiChoiceControlDescriptor,
                                                                      toChoices(multiChoiceControlDescriptor.getSource()),
                                                                      vals));
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return values.stream()
                             // Allow IRIs which correspond to entities
                             .filter(p -> p instanceof OWLEntity || p instanceof IRI)
                             .flatMap(p -> {
                                 if (p instanceof OWLEntity) {
                                     return Stream.of((OWLEntity) p);
                                 } else {
                                     var iri = (IRI) p;
                                     return entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(iri);
                                 }
                             })
                             .map(entity -> getRendering(entity))
                             .map(entity -> EntityNameControlDataDto.get(entityNameControlDescriptor, entity))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(ImageControlDescriptor imageControlDescriptor) {
                return values.stream()
                             .filter(p -> p instanceof IRI)
                             .map(p -> (IRI) p)
                             .map(iri -> ImageControlDataDto.get(imageControlDescriptor, iri))
                             .collect(toImmutableList());
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(GridControlDescriptor gridControlDescriptor) {
                return ImmutableList.of(toGridControlData(subject,
                                                          formFieldId,
                                                          values,
                                                          gridControlDescriptor,
                                                          formPageRequestIndex,
                                                          langTagFilter));
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(SubFormControlDescriptor subFormControlDescriptor) {
                // TODO: CHECK FOR CYCLES
                FormDescriptor subFormDescriptor = subFormControlDescriptor.getFormDescriptor();
                return values.stream()
                             .filter(p -> p instanceof OWLEntity)
                             .map(p -> (OWLEntity) p)
                             .map(entity -> toFormData(entity, subFormDescriptor, formPageRequestIndex, langTagFilter))
                             .collect(toImmutableList());
            }
        });
    }

    private ImmutableList<ChoiceDescriptorDto> toChoices(@Nonnull ChoiceListSourceDescriptor sourceDescriptor) {
        return choiceDescriptorDtoSupplier.getChoices(sourceDescriptor, sessionRenderer);
    }

    public FormDataDto toFormData(@Nonnull OWLEntity subject,
                               @Nonnull FormDescriptor formDescriptor,
                               @Nonnull FormPageRequestIndex formPageRequestIndex,
                               @Nonnull LangTagFilter langTagFilter) {
        var subjectData = getRendering(subject);
        var formSubject = getFormSubject(subjectData);
        var fieldData = formDescriptor.getFields()
                                      .stream()
                                      .map(field -> {
                                          ImmutableList<FormControlDataDto> formControlValues = toFormControlValues(subjectData,
                                                                                                                 field.getId(),
                                                                                                                 field,
                                                                                                                 formPageRequestIndex,
                                                                                                                 langTagFilter);
                                          var controlValuesStream = formControlValues
                                                  .stream()
                                                  .filter(fcv -> isIncluded(fcv, langTagFilter));
                                          var pageRequest = formPageRequestIndex.getPageRequest(formSubject.toFormSubject(),
                                                                                                field.getId(),
                                                                                                FormPageRequest.SourceType.CONTROL_STACK);
                                          var controlValuesPage = controlValuesStream.collect(PageCollector.toPage(
                                                  pageRequest.getPageNumber(),
                                                  pageRequest.getPageSize()
                                          ))
                                                                                     .orElse(Page.emptyPage());
                                          return FormFieldDataDto.get(field, controlValuesPage);
                                      })
                                      .collect(toImmutableList());
        return FormDataDto.get(formSubject, formDescriptor, fieldData);
    }

    private GridControlDataDto toGridControlData(OWLPrimitiveData root,
                                                 FormRegionId formFieldId,
                                                 ImmutableList<OWLPrimitive> subjects,
                                                 GridControlDescriptor gridControlDescriptor,
                                                 @Nonnull FormPageRequestIndex formPageRequestIndex,
                                                 @Nonnull LangTagFilter langTagFilter) {
        var rootSubject = getFormSubject(root);
        var pageRequest = formPageRequestIndex.getPageRequest(rootSubject.toFormSubject(),
                                                              formFieldId,
                                                              FormPageRequest.SourceType.GRID_CONTROL);
        var rowData = subjects.stream()
                              .map(this::toEntityFormSubject)
                              .filter(Objects::nonNull)
                              .map(entity -> toGridRow(entity, gridControlDescriptor, formPageRequestIndex,  langTagFilter))
                              .filter(Objects::nonNull)
                              .sorted()
                              .collect(PageCollector.toPage(pageRequest.getPageNumber(),
                                                            pageRequest.getPageSize()));
        return GridControlDataDto.get(gridControlDescriptor, rowData.orElse(Page.emptyPage()));
    }

    /**
     * Generate a row of a grid for the specified row subject
     * @param rowSubject The row subject
     * @param gridControlDescriptor The grid control descriptor
     * @return null if there is no row for the specified subject (because it is filtered out)
     */
    @Nullable
    private GridRowDataDto toGridRow(OWLEntityData rowSubject,
                                     GridControlDescriptor gridControlDescriptor,
                                     @Nonnull FormPageRequestIndex formPageRequestIndex,
                                     @Nonnull LangTagFilter langTagFilter) {
        var columnDescriptors = gridControlDescriptor.getColumns();
        // To Cells
        var cellData = toGridRowCells(rowSubject,
                                      columnDescriptors,
                                      formPageRequestIndex,
                                      langTagFilter);
        if(cellData.isEmpty()) {
            return null;
        }
        var formSubject = FormEntitySubjectDto.get(rowSubject);
        return GridRowDataDto.get(formSubject, cellData);
    }

    private ImmutableList<GridCellDataDto> toGridRowCells(OWLEntityData rowSubject,
                                                       @Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors,
                                                       @Nonnull FormPageRequestIndex formPageRequestIndex,
                                                       @Nonnull LangTagFilter langTagFilter) {
        var resultBuilder = ImmutableList.<GridCellDataDto>builder();
        for(var columnDescriptor : columnDescriptors) {
            var formControlData = toFormControlValues(
                    rowSubject,
                    columnDescriptor.getId(),
                    columnDescriptor,
                    formPageRequestIndex,
                    langTagFilter);
            if(formControlData.isEmpty()) {
                var cellData = GridCellDataDto.get(columnDescriptor.getId(), ImmutableList.of());
                resultBuilder.add(cellData);
            }
            else {
                if(columnDescriptor.getRepeatability() == Repeatability.NON_REPEATABLE) {
                    var firstValue = formControlData.get(0);
                    if(isIncluded(firstValue, langTagFilter)) {
                        var cellData = GridCellDataDto.get(columnDescriptor.getId(),
                                                        ImmutableList.of(firstValue));
                        resultBuilder.add(cellData);
                    }
                    else {
                        return ImmutableList.of();
                    }

                }
                else {
                    var cellData = GridCellDataDto.get(columnDescriptor.getId(),
                                            formControlData);
                    resultBuilder.add(cellData);
                }
            }
        }
        return resultBuilder.build();
    }

    private boolean isIncluded(@Nonnull FormControlDataDto formControlData,
                               @Nonnull LangTagFilter langTagFilter) {
        FormControlDataLangTagBasedInclusion formControlDataLangTagBasedInclusion = new FormControlDataLangTagBasedInclusion(langTagFilter);
        return formControlDataLangTagBasedInclusion.isIncluded(formControlData);

    }
}
