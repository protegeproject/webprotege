package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.form.data.FormControlDataDtoComparator;
import edu.stanford.bmir.protege.web.server.form.data.GridRowDataDtoComparatorFactory;
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
import static dagger.internal.codegen.DaggerStreams.toImmutableList;

@FormDataBuilderSession
public class GridControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider;

    @Nonnull
    private final FormDataBuilderSessionRenderer sessionRenderer;

    @Nonnull
    private final FormRegionOrderingIndex formRegionOrderingIndex;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Nonnull
    private final FormPageRequestIndex formPageRequestIndex;

    @Nonnull
    private final GridRowDataDtoComparatorFactory comparatorFactory;

    @Nonnull
    private final FormControlDataDtoComparator formControlDataDtoComparator;


    @Inject
    public GridControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                    @Nonnull Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider,
                                    @Nonnull FormDataBuilderSessionRenderer sessionRenderer,
                                    @Nonnull FormRegionOrderingIndex formRegionOrderingIndex,
                                    @Nonnull LangTagFilter langTagFilter,
                                    @Nonnull FormPageRequestIndex formPageRequestIndex,
                                    @Nonnull GridRowDataDtoComparatorFactory comparatorFactory, @Nonnull FormControlDataDtoComparator formControlDataDtoComparator) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.formDataDtoBuilderProvider = checkNotNull(formDataDtoBuilderProvider);
        this.sessionRenderer = checkNotNull(sessionRenderer);
        this.formRegionOrderingIndex = formRegionOrderingIndex;
        this.langTagFilter = langTagFilter;
        this.formPageRequestIndex = formPageRequestIndex;
        this.comparatorFactory = comparatorFactory;
        this.formControlDataDtoComparator = formControlDataDtoComparator;
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getGridControlDataDtoValues(@Nonnull GridControlDescriptor gridControlDescriptor,
                                                                         @Nonnull OWLEntityData subject,
                                                                         @Nonnull OwlBinding theBinding,
                                                                         @Nonnull FormRegionId formFieldId,
                                                                         int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        return ImmutableList.of(toGridControlData(subject,
                                                  formFieldId,
                                                  values,
                                                  gridControlDescriptor, depth));
    }


    private GridControlDataDto toGridControlData(OWLPrimitiveData root,
                                                 FormRegionId formFieldId,
                                                 ImmutableList<OWLPrimitive> subjects,
                                                 GridControlDescriptor descriptor, int depth) {
        var rootSubject = FormSubjectDto.getFormSubject(root);
        var pageRequest = formPageRequestIndex.getPageRequest(rootSubject.toFormSubject(),
                                                              formFieldId,
                                                              FormPageRequest.SourceType.GRID_CONTROL);
        var comparator = comparatorFactory.get(descriptor, Optional.empty());
        var rowData = subjects.stream()
                              .map(this::toEntityFormSubject)
                              .filter(Objects::nonNull)
                              .map(entity -> toGridRow(entity, descriptor, depth))
                              .filter(Objects::nonNull)
                              .sorted(comparator)
                              .collect(PageCollector.toPage(pageRequest.getPageNumber(),
                                                            pageRequest.getPageSize()));
        var orderings = formRegionOrderingIndex.getOrderings();
        if(orderings.isEmpty()) {
            orderings = descriptor.getColumns()
                      .stream()
                      .findFirst()
                      .map(GridColumnDescriptor::getId)
                      .map(columnId -> FormRegionOrdering.get(columnId, FormRegionOrderingDirection.ASC))
                      .map(ImmutableSet::of)
                    .orElse(ImmutableSet.of());
        }
        return GridControlDataDto.get(descriptor,
                                      rowData.orElse(Page.emptyPage()),
                                      orderings,
                                      depth);
    }

    /**
     * Generate a row of a grid for the specified row subject
     *
     * @param rowSubject            The row subject
     * @param gridControlDescriptor The grid control descriptor
     * @param depth
     * @return null if there is no row for the specified subject (because it is filtered out)
     */
    @Nullable
    private GridRowDataDto toGridRow(OWLEntityData rowSubject,
                                     GridControlDescriptor gridControlDescriptor,
                                     int depth) {
        var columnDescriptors = gridControlDescriptor.getColumns();
        // To Cells
        var cellData = toGridRowCells(rowSubject,
                                      columnDescriptors,
                                      depth);
        if (cellData.isEmpty()) {
            return null;
        }
        var formSubject = FormEntitySubjectDto.get(rowSubject);
        return GridRowDataDto.get(formSubject, cellData);
    }

    @Nonnull
    private ImmutableList<GridCellDataDto> toGridRowCells(OWLEntityData rowSubject,
                                                          @Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors,
                                                          int depth) {
        var resultBuilder = ImmutableList.<GridCellDataDto>builder();
        for (var columnDescriptor : columnDescriptors) {
            var columnId = columnDescriptor.getId();
            var direction = formRegionOrderingIndex.getOrderingDirection(columnId);
            var comp = direction.isAscending() ? formControlDataDtoComparator : formControlDataDtoComparator.reversed();
            var cellValues = formDataDtoBuilderProvider.get()
                                                       .toFormControlValues(rowSubject, columnId, columnDescriptor, depth + 1)
                    .stream()
                    .sorted(comp)
                    .collect(toImmutableList());

            if (cellValues.isEmpty()) {
                var cellData = GridCellDataDto.get(columnId, Page.emptyPage());
                resultBuilder.add(cellData);
            }
            else {
                if (columnDescriptor.getRepeatability() == Repeatability.NON_REPEATABLE) {
                    var firstValue = cellValues.get(0);
                    if (isIncluded(firstValue)) {
                        var cellData = GridCellDataDto.get(columnId,
                                                           Page.of(firstValue));
                        resultBuilder.add(cellData);
                    }
                    else {
                        return ImmutableList.of();
                    }

                }
                else {
                    var cellData = GridCellDataDto.get(columnId,
                                                       new Page<>(1, 1, cellValues, cellValues.size()));
                    resultBuilder.add(cellData);
                }
            }
        }
        return resultBuilder.build();
    }

    private boolean isIncluded(FormControlDataDto firstValue) {
        if (firstValue instanceof TextControlDataDto) {
            return ((TextControlDataDto) firstValue).getValue()
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
        }
        else if (primitive instanceof IRI) {
            var iri = (IRI) primitive;
            return sessionRenderer.getRendering(iri)
                                  .stream()
                                  .sorted()
                                  .findFirst()
                                  .orElse(null);
        }
        else {
            return null;
        }
    }


}