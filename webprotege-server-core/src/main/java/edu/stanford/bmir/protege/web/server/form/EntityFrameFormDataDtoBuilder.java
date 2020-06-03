package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
@FormDataBuilderSession
public class EntityFrameFormDataDtoBuilder {

    @Nonnull
    private final FormDataBuilderSessionRenderer sessionRenderer;

    @Nonnull
    private final TextControlValuesBuilder textControlValuesBuilder;

    @Nonnull
    private final NumberControlValuesBuilder numberControlValuesBuilder;

    @Nonnull
    private final MultiChoiceControlValueBuilder multiChoiceControlValueBuilder;

    @Nonnull
    private final SingleChoiceControlValuesBuilder SingleChoiceControlValuesBuilder;

    @Nonnull
    private final EntityNameControlValuesBuilder entityNameControlValuesBuilder;

    @Nonnull
    private final ImageControlValuesBuilder imageControlValuesBuilder;

    @Nonnull
    private final GridControlValuesBuilder gridControlValuesBuilder;

    @Nonnull
    private final SubFormControlValuesBuilder subFormControlValuesBuilder;

    @Inject
    public EntityFrameFormDataDtoBuilder(@Nonnull FormDataBuilderSessionRenderer sessionRenderer,
                                         @Nonnull TextControlValuesBuilder textControlValuesBuilder,
                                         @Nonnull NumberControlValuesBuilder numberControlValuesBuilder,
                                         @Nonnull MultiChoiceControlValueBuilder multiChoiceControlValueBuilder,
                                         @Nonnull SingleChoiceControlValuesBuilder singleChoiceControlValuesBuilder,
                                         @Nonnull EntityNameControlValuesBuilder entityNameControlValuesBuilder,
                                         @Nonnull ImageControlValuesBuilder imageControlValuesBuilder,
                                         @Nonnull GridControlValuesBuilder gridControlValuesBuilder,
                                         @Nonnull SubFormControlValuesBuilder subFormControlValuesBuilder) {
        this.sessionRenderer = sessionRenderer;
        this.textControlValuesBuilder = textControlValuesBuilder;
        this.numberControlValuesBuilder = numberControlValuesBuilder;
        this.multiChoiceControlValueBuilder = multiChoiceControlValueBuilder;
        SingleChoiceControlValuesBuilder = singleChoiceControlValuesBuilder;
        this.entityNameControlValuesBuilder = entityNameControlValuesBuilder;
        this.imageControlValuesBuilder = imageControlValuesBuilder;
        this.gridControlValuesBuilder = gridControlValuesBuilder;
        this.subFormControlValuesBuilder = subFormControlValuesBuilder;
    }

    @Nonnull
    protected ImmutableList<FormControlDataDto> toFormControlValues(@Nonnull OWLEntityData subject,
                                                                  @Nonnull FormRegionId formFieldId,
                                                                  @Nonnull BoundControlDescriptor descriptor,
                                                                  @Nonnull FormPageRequestIndex formPageRequestIndex,
                                                                  @Nonnull LangTagFilter langTagFilter,
                                                                  @Nonnull Optional<GridControlOrdering> regionOrdering,
                                                                  @Nonnull ImmutableList<GridControlOrdering> orderings) {
        var owlBinding = descriptor.getOwlBinding();
        if (owlBinding.isEmpty()) {
            return ImmutableList.of();
        }
        var theBinding = owlBinding.get();
        var formControlDescriptor = descriptor.getFormControlDescriptor();
        return formControlDescriptor.accept(new FormControlDescriptorVisitor<>() {
            @Override
            public ImmutableList<FormControlDataDto> visit(TextControlDescriptor textControlDescriptor) {
                return textControlValuesBuilder.getTextControlDataDtoValues(textControlDescriptor,
                                                                            subject,
                                                                            theBinding,
                                                                            langTagFilter);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(NumberControlDescriptor numberControlDescriptor) {
                return numberControlValuesBuilder.getNumberControlDataDtoValues(numberControlDescriptor,
                                                                                subject,
                                                                                theBinding);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                return SingleChoiceControlValuesBuilder.getSingleChoiceControlDataDtoValues(singleChoiceControlDescriptor,
                                                                                            subject,
                                                                                            theBinding,
                                                                                            langTagFilter);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                return multiChoiceControlValueBuilder.getMultiChoiceControlDataDtoValues(multiChoiceControlDescriptor,
                                                                                         subject,
                                                                                         theBinding);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return entityNameControlValuesBuilder.getEntityNameControlDataDtoValues(entityNameControlDescriptor,
                                                                                        subject,
                                                                                        theBinding);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(ImageControlDescriptor imageControlDescriptor) {
                return imageControlValuesBuilder.getImageControlDataDtoValues(imageControlDescriptor,
                                                                              subject,
                                                                              theBinding);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(GridControlDescriptor gridControlDescriptor) {
                return gridControlValuesBuilder.getGridControlDataDtoValues(gridControlDescriptor,
                                                                            subject,
                                                                            theBinding,
                                                                            formFieldId,
                                                                            formPageRequestIndex,
                                                                            langTagFilter,
                                                                            regionOrdering,
                                                                            orderings);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(SubFormControlDescriptor subFormControlDescriptor) {
                return subFormControlValuesBuilder.getSubFormControlDataDtoValues(subFormControlDescriptor,
                                                                                  subject,
                                                                                  theBinding,
                                                                                  formPageRequestIndex,
                                                                                  langTagFilter,
                                                                                  orderings);
            }
        });
    }


    public FormDataDto toFormData(@Nonnull OWLEntity subject,
                                  @Nonnull FormDescriptor formDescriptor,
                                  @Nonnull FormPageRequestIndex formPageRequestIndex,
                                  @Nonnull LangTagFilter langTagFilter,
                                  @Nonnull ImmutableList<GridControlOrdering> orderings) {
        var orderingMap = orderings.stream()
                                   .collect(toMap(GridControlOrdering::getFieldId,
                                                  ordering -> ordering,
                                                  (left, right) -> left));
        var subjectData = sessionRenderer.getEntityRendering(subject);
        var formSubject = FormSubjectDto.getFormSubject(subjectData);
        var fieldData = formDescriptor.getFields()
                                      .stream()
                                      .map(field -> {
                                          var ordering = Optional.ofNullable(orderingMap.get(field.getId()));
                                          var formControlValues = toFormControlValues(subjectData,
                                                                                      field.getId(),
                                                                                      field,
                                                                                      formPageRequestIndex,
                                                                                      langTagFilter,
                                                                                      ordering,
                                                                                      orderings);
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

    private boolean isIncluded(@Nonnull FormControlDataDto formControlData,
                               @Nonnull LangTagFilter langTagFilter) {
        FormControlDataLangTagBasedInclusion formControlDataLangTagBasedInclusion = new FormControlDataLangTagBasedInclusion(
                langTagFilter);
        return formControlDataLangTagBasedInclusion.isIncluded(formControlData);

    }
}
