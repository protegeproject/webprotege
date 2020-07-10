package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.form.data.FormControlDataDtoComparator;
import edu.stanford.bmir.protege.web.server.form.data.GridRowDataDtoComparatorFactory;
import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.FilterState;
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
import static java.util.stream.Collectors.partitioningBy;

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

    @Nonnull
    private final FormRegionFilterPredicateManager filters;


    @Inject
    public GridControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                    @Nonnull Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider,
                                    @Nonnull FormDataBuilderSessionRenderer sessionRenderer,
                                    @Nonnull FormRegionOrderingIndex formRegionOrderingIndex,
                                    @Nonnull LangTagFilter langTagFilter,
                                    @Nonnull FormPageRequestIndex formPageRequestIndex,
                                    @Nonnull GridRowDataDtoComparatorFactory comparatorFactory,
                                    @Nonnull FormControlDataDtoComparator formControlDataDtoComparator,
                                    @Nonnull FormRegionFilterIndex formRegionFilterIndex,
                                    @Nonnull FormFilterMatcherFactory formFilterMatcherFactory, @Nonnull FormRegionFilterPredicateManager filters) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.formDataDtoBuilderProvider = checkNotNull(formDataDtoBuilderProvider);
        this.sessionRenderer = checkNotNull(sessionRenderer);
        this.formRegionOrderingIndex = formRegionOrderingIndex;
        this.langTagFilter = langTagFilter;
        this.formPageRequestIndex = formPageRequestIndex;
        this.comparatorFactory = comparatorFactory;
        this.formControlDataDtoComparator = formControlDataDtoComparator;
        this.filters = filters;
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

    private FilterState getFilterState(@Nonnull GridControlDescriptor gridControlDescriptor) {
        var filtered = gridControlDescriptor.getColumns()
                                            .stream()
                                            .flatMap(GridColumnDescriptor::getLeafColumnIds)
                                            .anyMatch(filters::isFiltered);
        if (filtered) {
            return FilterState.FILTERED;
        }
        else {
            return FilterState.UNFILTERED;
        }
    }


    private GridControlDataDto toGridControlData(OWLPrimitiveData root,
                                                 FormRegionId formFieldId,
                                                 ImmutableList<OWLPrimitive> subjects,
                                                 GridControlDescriptor descriptor, int depth) {
        if (subjects.isEmpty()) {
            var filterState = getFilterState(descriptor);
            return GridControlDataDto.get(descriptor,
                                          Page.emptyPage(),
                                          formRegionOrderingIndex.getOrderings(),
                                          depth,
                                          filterState);
        }

        var rootSubject = FormSubjectDto.getFormSubject(root);
        var pageRequest = formPageRequestIndex.getPageRequest(rootSubject.toFormSubject(),
                                                              formFieldId,
                                                              FormPageRequest.SourceType.GRID_CONTROL);
        var comparator = comparatorFactory.get(descriptor, Optional.empty());

        var rowsPage = subjects.stream()
                               .map(this::toEntityFormSubject)
                               .filter(Objects::nonNull)
                               .map(entity -> toGridRow(entity, descriptor, depth))
                               .filter(row -> !row.containsFilteredEmptyCells())
                               .sorted(comparator)
                               .collect(PageCollector.toPage(pageRequest.getPageNumber(),
                                                             pageRequest.getPageSize()))
                               .orElse(Page.emptyPage());
        var orderings = formRegionOrderingIndex.getOrderings();
        if (orderings.isEmpty()) {
            orderings = descriptor.getColumns()
                                  .stream()
                                  .findFirst()
                                  .map(GridColumnDescriptor::getId)
                                  .map(columnId -> FormRegionOrdering.get(columnId, FormRegionOrderingDirection.ASC))
                                  .map(ImmutableSet::of)
                                  .orElse(ImmutableSet.of());
        }
        var filterState = getFilterState(descriptor);
        return GridControlDataDto.get(descriptor,
                                      rowsPage,
                                      orderings,
                                      depth,
                                      filterState);
    }

    /**
     * Generate a row of a grid for the specified row subject
     *
     * @param rowSubject            The row subject
     * @param gridControlDescriptor The grid control descriptor
     * @return null if there is no row for the specified subject (because it is filtered out)
     */
    @Nonnull
    private GridRowDataDto toGridRow(OWLEntityData rowSubject,
                                     GridControlDescriptor gridControlDescriptor,
                                     int depth) {
        var columnDescriptors = gridControlDescriptor.getColumns();
        var formSubject = FormEntitySubjectDto.get(rowSubject);
        // To Cells
        var cellData = toGridRowCells(rowSubject,
                                      columnDescriptors,
                                      depth);
        return GridRowDataDto.get(formSubject, cellData);
    }

    @Nonnull
    private ImmutableList<GridCellDataDto> toGridRowCells(OWLEntityData rowSubject,
                                                          @Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors,
                                                          int depth) {
        return columnDescriptors.stream()
                                .map(cd -> toGridCellData(rowSubject, depth, cd))
                                .collect(toImmutableList());
    }

    private GridCellDataDto toGridCellData(OWLEntityData rowSubject, int depth, GridColumnDescriptor columnDescriptor) {
        var columnId = columnDescriptor.getId();
        var direction = formRegionOrderingIndex.getOrderingDirection(columnId);
        var cellValueComparator = direction.isAscending() ? formControlDataDtoComparator : formControlDataDtoComparator.reversed();
        var filterPredicate = filters.getFilterPredicate(columnId);
        var cellValues = formDataDtoBuilderProvider.get()
                                                   .toFormControlValues(rowSubject,
                                                                        columnId,
                                                                        columnDescriptor,
                                                                        depth + 1)
                                                   .stream()
                                                   .filter(GridControlValuesBuilder::isNotEmptyGrid)
                                                   .filter(filterPredicate)
                                                   .sorted(cellValueComparator)
                                                   .collect(toImmutableList());
        var filterState = getFilterState(columnDescriptor);
        // The combined filter state takes into consideration the fact that global lang tag
        // filtering may be in place
        var combinedFilterState = getCombinedFilterState(filterState);
        if (cellValues.isEmpty()) {
            return GridCellDataDto.get(columnId, Page.emptyPage(), combinedFilterState);
        }
        if (columnDescriptor.isRepeatable()) {
            return GridCellDataDto.get(columnId, Page.of(cellValues), combinedFilterState);
        }
        // There should only be one to return, but we allow for the fact that
        // there could be more than one – after sorting we take the first one
        var firstValue = cellValues.get(0);
        if (isIncluded(firstValue)) {
            return GridCellDataDto.get(columnId,
                                       Page.of(firstValue),
                                       combinedFilterState);
        }
        return GridCellDataDto.get(columnId, Page.emptyPage(), combinedFilterState);
    }

    private FilterState getCombinedFilterState(FilterState gridFilterState) {
        if(gridFilterState.equals(FilterState.UNFILTERED)) {
            if(langTagFilter.isFilterActive()) {
                return FilterState.FILTERED;
            }
        }
        return gridFilterState;
    }

    private static boolean isNotEmptyGrid(FormControlDataDto value) {
        return !(value instanceof GridControlDataDto) || !((GridControlDataDto) value).isFilteredEmpty();
    }

    /**
     * Get the filter state for the specified column
     *
     * @param descriptor The column descriptor
     * @return The filter state.  If the column is filtered, or it contains a nested grid that
     * has one or more filtered columns then the state is FILTERED, otherwise it is UNFILTERED
     */
    private FilterState getFilterState(@Nonnull GridColumnDescriptor descriptor) {
        boolean filtered = descriptor.getLeafColumnIds().anyMatch(filters::isFiltered);
        if (filtered) {
            return FilterState.FILTERED;
        }
        else {
            return FilterState.UNFILTERED;
        }
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