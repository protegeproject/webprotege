package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.form.data.FormControlDataDtoComparator;
import edu.stanford.bmir.protege.web.server.form.data.GridRowDataDtoComparatorFactory;
import edu.stanford.bmir.protege.web.server.pagination.PageCollector;
import edu.stanford.bmir.protege.web.shared.form.FilterState;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
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

    @Nonnull
    private final OWLDataFactory dataFactory;


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
                                    @Nonnull FormFilterMatcherFactory formFilterMatcherFactory,
                                    @Nonnull FormRegionFilterPredicateManager filters,
                                    @Nonnull OWLDataFactory dataFactory) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.formDataDtoBuilderProvider = checkNotNull(formDataDtoBuilderProvider);
        this.sessionRenderer = checkNotNull(sessionRenderer);
        this.formRegionOrderingIndex = formRegionOrderingIndex;
        this.langTagFilter = langTagFilter;
        this.formPageRequestIndex = formPageRequestIndex;
        this.comparatorFactory = comparatorFactory;
        this.formControlDataDtoComparator = formControlDataDtoComparator;
        this.filters = filters;
        this.dataFactory = dataFactory;
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getGridControlDataDtoValues(@Nonnull GridControlDescriptor gridControlDescriptor,
                                                                         @Nonnull Optional<FormEntitySubject>
                                                                                 subject,
                                                                         @Nonnull OwlBinding theBinding,
                                                                         @Nonnull FormRegionId formFieldId,
                                                                         int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject, theBinding);
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


    private GridControlDataDto toGridControlData(Optional<FormEntitySubject> rootSubject,
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

        var pageRequest = rootSubject.map(s -> formPageRequestIndex.getPageRequest(s,
                                                                               formFieldId,
                                                                               FormPageRequest.SourceType.GRID_CONTROL)).orElseGet(PageRequest::requestFirstPage);
        var comparator = comparatorFactory.get(descriptor, Optional.empty());

        var subjectFactoryDescriptor = descriptor.getSubjectFactoryDescriptor();

        var rowsPage = subjects.stream()
                               .map(s -> toEntityFormSubject(s, subjectFactoryDescriptor.orElseThrow().getEntityType()))
                               .map(s -> toGridRow(s, descriptor, depth))
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
    private GridRowDataDto toGridRow(Optional<FormEntitySubject> rowSubject,
                                     GridControlDescriptor gridControlDescriptor,
                                     int depth) {
        var columnDescriptors = gridControlDescriptor.getColumns();
        var formSubject = rowSubject.map(s -> FormEntitySubjectDto.get(sessionRenderer.getEntityRendering(s.getEntity())));
        // To Cells
        var cellData = toGridRowCells(rowSubject,
                                      columnDescriptors,
                                      depth);
        return formSubject.map(s -> GridRowDataDto.get(s, cellData)).orElseGet(() -> GridRowDataDto.get(cellData));
    }

    @Nonnull
    private ImmutableList<GridCellDataDto> toGridRowCells(Optional<FormEntitySubject> rowSubject,
                                                          @Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors,
                                                          int depth) {
        return columnDescriptors.stream()
                                .map(cd -> toGridCellData(rowSubject, depth, cd))
                                .collect(toImmutableList());
    }

    private GridCellDataDto toGridCellData(Optional<FormEntitySubject> rowSubject, int depth, GridColumnDescriptor columnDescriptor) {
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


    @Nonnull
    private Optional<FormEntitySubject> toEntityFormSubject(@Nonnull OWLPrimitive value,
                                                            @Nonnull EntityType<?> rowEntityType) {
        if(value instanceof OWLEntity) {
            return Optional.of(FormEntitySubject.get((OWLEntity) value));
        }
        else if(value instanceof IRI) {
            var entity = dataFactory.getOWLEntity(rowEntityType, (IRI) value);
            return Optional.of(FormEntitySubject.get(entity));
        }
        else {
            return Optional.empty();
        }
    }


}