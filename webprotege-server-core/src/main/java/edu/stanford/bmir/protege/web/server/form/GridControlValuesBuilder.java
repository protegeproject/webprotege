package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.form.field.GridControlOrderByDirection.ASC;

@FormDataBuilderSession
public class GridControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final GridRowDataDtoComparatorFactory gridRowDataDtoComparatorFactory;

    @Nonnull
    private final Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider;

    @Nonnull
    private final FormDataBuilderSessionRenderer sessionRenderer;

    @Inject
    public GridControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                    @Nonnull GridRowDataDtoComparatorFactory gridRowDataDtoComparatorFactory,
                                    @Nonnull Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider,
                                    @Nonnull FormDataBuilderSessionRenderer sessionRenderer) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.gridRowDataDtoComparatorFactory = checkNotNull(gridRowDataDtoComparatorFactory);
        this.formDataDtoBuilderProvider = checkNotNull(formDataDtoBuilderProvider);
        this.sessionRenderer = checkNotNull(sessionRenderer);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getGridControlDataDtoValues(GridControlDescriptor gridControlDescriptor, @Nonnull OWLEntityData subject, OwlBinding theBinding, @Nonnull FormRegionId formFieldId, @Nonnull FormPageRequestIndex formPageRequestIndex, @Nonnull LangTagFilter langTagFilter, @Nonnull Optional<GridControlOrdering> regionOrdering, @Nonnull ImmutableList<GridControlOrdering> orderings) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        return ImmutableList.of(toGridControlData(subject,
                                                  formFieldId,
                                                  values,
                                                  gridControlDescriptor,
                                                  formPageRequestIndex,
                                                  langTagFilter,
                                                  regionOrdering,
                                                  orderings));
    }


    private GridControlDataDto toGridControlData(OWLPrimitiveData root,
                                                 FormRegionId formFieldId,
                                                 ImmutableList<OWLPrimitive> subjects,
                                                 GridControlDescriptor gridControlDescriptor,
                                                 @Nonnull FormPageRequestIndex formPageRequestIndex,
                                                 @Nonnull LangTagFilter langTagFilter,
                                                 @Nonnull Optional<GridControlOrdering> regionOrdering,
                                                 @Nonnull ImmutableList<GridControlOrdering> orderings) {
        var rootSubject = FormSubjectDto.getFormSubject(root);
        var pageRequest = formPageRequestIndex.getPageRequest(rootSubject.toFormSubject(),
                                                              formFieldId,
                                                              FormPageRequest.SourceType.GRID_CONTROL);
        var comparator = regionOrdering.filter(ordering -> ordering.getFieldId().equals(formFieldId))
                                       .map(ordering -> gridRowDataDtoComparatorFactory.get(ordering.getOrdering(),
                                                                                            gridControlDescriptor))
                                       .orElseGet(() -> gridRowDataDtoComparatorFactory.get(ImmutableList.of(),
                                                                                            gridControlDescriptor));
        var rowData = subjects.stream()
                              .map(this::toEntityFormSubject)
                              .filter(Objects::nonNull)
                              .map(entity -> toGridRow(entity,
                                                       gridControlDescriptor,
                                                       formPageRequestIndex,
                                                       langTagFilter,
                                                       orderings))
                              .filter(Objects::nonNull)
                              .sorted(comparator)
                              .collect(PageCollector.toPage(pageRequest.getPageNumber(),
                                                            pageRequest.getPageSize()));
        var gridOrdering = regionOrdering.map(GridControlOrdering::getOrdering)
                                         .orElseGet(() -> {
                                             return gridControlDescriptor.getLeafColumns()
                                                                         .map(GridColumnDescriptor::getId)
                                                                         .map(id -> GridControlOrderBy.get(id, ASC))
                                                                         .limit(1)
                                                                         .collect(toImmutableList());
                                         });
        return GridControlDataDto.get(gridControlDescriptor, rowData.orElse(Page.emptyPage()), gridOrdering);
    }

    /**
     * Generate a row of a grid for the specified row subject
     *
     * @param rowSubject            The row subject
     * @param gridControlDescriptor The grid control descriptor
     * @return null if there is no row for the specified subject (because it is filtered out)
     */
    @Nullable
    private GridRowDataDto toGridRow(OWLEntityData rowSubject,
                                     GridControlDescriptor gridControlDescriptor,
                                     @Nonnull FormPageRequestIndex formPageRequestIndex,
                                     @Nonnull LangTagFilter langTagFilter,
                                     @Nonnull ImmutableList<GridControlOrdering> orderings) {
        var columnDescriptors = gridControlDescriptor.getColumns();
        // To Cells
        var cellData = toGridRowCells(rowSubject,
                                      columnDescriptors,
                                      formPageRequestIndex,
                                      langTagFilter,
                                      orderings);
        if (cellData.isEmpty()) {
            return null;
        }
        var formSubject = FormEntitySubjectDto.get(rowSubject);
        return GridRowDataDto.get(formSubject, cellData);
    }

    @Nonnull
    private ImmutableList<GridCellDataDto> toGridRowCells(OWLEntityData rowSubject,
                                                          @Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors,
                                                          @Nonnull FormPageRequestIndex formPageRequestIndex,
                                                          @Nonnull LangTagFilter langTagFilter,
                                                          @Nonnull ImmutableList<GridControlOrdering> orderings) {
        var resultBuilder = ImmutableList.<GridCellDataDto>builder();
        for (var columnDescriptor : columnDescriptors) {
            var formControlData = formDataDtoBuilderProvider.get().toFormControlValues(
                    rowSubject,
                    columnDescriptor.getId(),
                    columnDescriptor,
                    formPageRequestIndex,
                    langTagFilter,
                    Optional.empty(),
                    orderings);
            if (formControlData.isEmpty()) {
                var cellData = GridCellDataDto.get(columnDescriptor.getId(), Page.emptyPage());
                resultBuilder.add(cellData);
            } else {
                if (columnDescriptor.getRepeatability() == Repeatability.NON_REPEATABLE) {
                    var firstValue = formControlData.get(0);
                    if (isIncluded(firstValue, langTagFilter)) {
                        var cellData = GridCellDataDto.get(columnDescriptor.getId(),
                                                           Page.of(firstValue));
                        resultBuilder.add(cellData);
                    } else {
                        return ImmutableList.of();
                    }

                } else {
                    var cellData = GridCellDataDto.get(columnDescriptor.getId(),
                                                       new Page<>(1, 1, formControlData, formControlData.size()));
                    resultBuilder.add(cellData);
                }
            }
        }
        return resultBuilder.build();
    }

    private boolean isIncluded(FormControlDataDto firstValue, LangTagFilter langTagFilter) {
        if(firstValue instanceof TextControlDataDto) {
            return  ((TextControlDataDto) firstValue).getValue()
                                                     .map(v -> langTagFilter.isIncluded(v.getLang()))
                                                     .orElse(false);
        }
        else {
            return true;
        }
    }


    @Nullable
    private OWLEntityData toEntityFormSubject(OWLPrimitive primitive) {
        if (primitive instanceof OWLEntity) {
            return sessionRenderer.getEntityRendering((OWLEntity) primitive);
        } else if (primitive instanceof IRI) {
            var iri = (IRI) primitive;
            return sessionRenderer.getRendering(iri)
                                  .stream()
                                  .sorted()
                                  .findFirst()
                                  .orElse(null);
        } else {
            return null;
        }
    }


}