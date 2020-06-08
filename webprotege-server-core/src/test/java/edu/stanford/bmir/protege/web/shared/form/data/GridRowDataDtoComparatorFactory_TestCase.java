package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.form.FormRegionOrderingIndex;
import edu.stanford.bmir.protege.web.server.form.data.GridCellDataDtoComparator;
import edu.stanford.bmir.protege.web.server.form.data.GridRowDataDtoComparatorFactory;
import edu.stanford.bmir.protege.web.server.form.data.GridRowDtoByColumnIndexComparator;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GridRowDataDtoComparatorFactory_TestCase {

    public static final Optionality REQUIRED = Optionality.REQUIRED;

    public static final Repeatability NON_REPEATABLE = Repeatability.NON_REPEATABLE;


    @Mock
    public LanguageMap columnLabel = LanguageMap.empty();

    @Mock
    public OwlBinding owlBinding;

    @Mock
    public FormSubjectFactoryDescriptor subjectFactoryDescriptor;


    private GridRowDataDtoComparatorFactory comparatorFactory;

    @Mock
    private GridCellDataDtoComparator gridCellDataDtoComparator;

    @Mock
    private FormRegionOrderingIndex orderingIndex;

    @Before
    public void setUp() throws Exception {
        comparatorFactory = new GridRowDataDtoComparatorFactory(gridCellDataDtoComparator,
                                                                orderingIndex);
    }

    @Test
    public void shouldCreateComparatorForFirstColumn() {
        var columnId = generateColumnId();
        var textControlDescriptor = TextControlDescriptor.getDefault();
        var desc = createGridControlDescriptorWithColumns(ImmutableMap.of(columnId, textControlDescriptor));
        var comparator = (GridRowDtoByColumnIndexComparator) createComparatorOrderByColumnIds(desc, columnId);
        var columnIndex = comparator.getColumnIndex();
        assertThat(columnIndex, equalTo(0));
    }

    @Test
    public void shouldCreateComparatorForSecondColumn() {
        var column0Id = generateColumnId();
        var column1Id = generateColumnId();
        var textControlDescriptor = TextControlDescriptor.getDefault();
        var gridControlDescriptor = createGridControlDescriptorWithColumns(ImmutableMap.of(column0Id, textControlDescriptor,
                                                              column1Id, textControlDescriptor));
        var comparator = (GridRowDtoByColumnIndexComparator) createComparatorOrderByColumnIds(gridControlDescriptor, column1Id);
        var columnIndex = comparator.getColumnIndex();
        assertThat(columnIndex, equalTo(1));
    }

    @Test
    public void shouldCreateComparatorForFirstColumnThenSecondColumn() {
        var column0Id = generateColumnId();
        var nestedColumn0Id = generateColumnId();
        var nestedColumn1Id = generateColumnId();
        var controlDescriptor = createGridControlDescriptorWithColumns(Map.of(nestedColumn0Id, TextControlDescriptor.getDefault(),
                                                                              nestedColumn1Id, TextControlDescriptor.getDefault()));
        var gridControlDescriptor = createGridControlDescriptorWithColumns(ImmutableMap.of(column0Id, controlDescriptor));
        var comparator = (GridRowDtoByColumnIndexComparator) createComparatorOrderByColumnIds(gridControlDescriptor, nestedColumn1Id);
        var columnIndex = comparator.getColumnIndex();
        assertThat(columnIndex, equalTo(0));
    }

    public Comparator<GridRowDataDto> createComparatorOrderByColumnIds(GridControlDescriptor gridControlDescriptor,
                                                                       GridColumnId ... columnIds) {
        var orderBys = Stream.of(columnIds)
              .map(columnId -> FormRegionOrdering.get(columnId, FormRegionOrderingDirection.ASC))
              .collect(toImmutableSet());
        when(orderingIndex.getOrderings())
                .thenReturn(orderBys);
        return comparatorFactory.get(gridControlDescriptor, Optional.empty());
    }


    private GridControlDescriptor createGridControlDescriptorWithColumns(Map<GridColumnId, FormControlDescriptor> colId2ControlDescriptor) {
        var columnDescriptors = colId2ControlDescriptor
                .entrySet()
                .stream()
              .map(k -> createGridColumnDescriptor(k.getValue(), k.getKey()))
              .collect(toImmutableList());
        var descriptors = ImmutableList.copyOf(columnDescriptors);
        return createGridControlDescriptor(descriptors);
    }



    public GridControlDescriptor createGridControlDescriptor(ImmutableList<GridColumnDescriptor> columnDescriptors) {
        return GridControlDescriptor.get(
                columnDescriptors,
                subjectFactoryDescriptor
        );
    }

    private GridColumnDescriptor createGridColumnDescriptor(FormControlDescriptor formControlDescriptor,
                                                            GridColumnId columnId) {
        return GridColumnDescriptor.get(
                columnId,
                REQUIRED,
                NON_REPEATABLE,
                owlBinding,
                columnLabel,
                formControlDescriptor
        );
    }

    public static GridColumnId generateColumnId() {
        return GridColumnId.get(UUID.randomUUID().toString());
    }
}