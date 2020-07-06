package edu.stanford.bmir.protege.web.server.form.processor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptorMissingException;
import edu.stanford.bmir.protege.web.shared.form.data.GridControlData;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowData;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@RunWith(MockitoJUnitRunner.class)
public class GridControlDataProcessor_TestCase {

    GridControlDataProcessor processor;

    @Mock
    private GridRowDataProcessor gridRowDataProcessor;

    @Mock
    private OwlBinding binding;

    @Mock
    private GridControlDescriptor gridControlDescriptor;

    private ImmutableList<GridColumnDescriptor> columns = ImmutableList.of(mock(GridColumnDescriptor.class));

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private FormSubjectFactoryDescriptor rowSubjectFactoryDescriptor;

    @Mock
    private GridRowData gridRowData;

    private Page<GridRowData> page;

    private GridControlData gridControlData;

    private ImmutableSet<FormRegionOrdering> ordering = ImmutableSet.of();

    @Before
    public void setUp() {
        processor = new GridControlDataProcessor(gridRowDataProcessor);
        when(gridControlDescriptor.getColumns())
                .thenReturn(columns);
        page = new Page<>(1, 1, ImmutableList.of(gridRowData), 1);
        gridControlData = GridControlData.get(gridControlDescriptor, page, ordering);
    }

    @Test
    public void shouldProcessGridRows() {
        when(gridControlDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.of(rowSubjectFactoryDescriptor));
        processor.processGridControlData(binding, gridControlData, formFrameBuilder);
        verify(gridRowDataProcessor, times(1))
                .processGridRowData(binding, rowSubjectFactoryDescriptor, columns, formFrameBuilder, gridRowData);
    }

    @Test(expected = FormSubjectFactoryDescriptorMissingException.class)
    public void shouldThrowExceptionForMissingRowSubjectFactoryDescriptor() {
        when(gridControlDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.empty());
        processor.processGridControlData(binding, gridControlData, formFrameBuilder);

    }
}
