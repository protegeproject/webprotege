package edu.stanford.bmir.protege.web.server.form.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormFieldBindingMissingException;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.GridColumnBindingMissingException;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellData;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowData;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@RunWith(MockitoJUnitRunner.class)
public class GridRowDataProcessor_TestCase {

    private GridRowDataProcessor processor;

    @Mock
    private FormFrameBuilder formFrameBuilder, rowformFrameBuilder;

    @Mock
    private GridCellDataProcessor gridCellDataProcessor;

    @Mock
    private OwlBinding binding;

    @Mock
    private FormSubjectFactoryDescriptor rowSubjectFactoryDescriptor;

    @Mock
    private GridColumnDescriptor columnDescriptor;

    @Mock
    private GridRowData gridRowData;

    @Mock
    private GridCellData gridCellData;

    @Mock
    private OwlBinding columnBinding;

    @Before
    public void setUp() {
        processor = new GridRowDataProcessor(() -> rowformFrameBuilder,
                                             gridCellDataProcessor);

        when(columnDescriptor.getId())
                .thenReturn(mock(GridColumnId.class));

        when(gridRowData.getCells())
                .thenReturn(ImmutableList.of(gridCellData));
        when(columnDescriptor.getOwlBinding())
                .thenReturn(Optional.of(columnBinding));
    }

    @Test
    public void shouldAddBindingForRow() {
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
        verify(formFrameBuilder, times(1))
                .add(binding, rowformFrameBuilder);
    }

    @Test
    public void shouldNotSetSubjectIfRowDoesNotHaveSubject() {
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
        verify(formFrameBuilder, never()).setSubject(any());
    }

    @Test
    public void shouldSetSubjectIfRowHasSubject() {
        var formSubject = mock(FormSubject.class);
        when(gridRowData.getSubject())
                .thenReturn(Optional.of(formSubject));
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
        var rowFrameCapture = ArgumentCaptor.forClass(FormFrameBuilder.class);
        verify(formFrameBuilder, times(1)).add(eq(binding), rowFrameCapture.capture());
        verify(rowFrameCapture.getValue(), times(1)).setSubject(formSubject);
    }

    @Test
    public void shouldAddBindingForCells() {
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
        verify(gridCellDataProcessor, times(1))
                .processGridCellData(rowformFrameBuilder, columnBinding, gridCellData);
    }

    @Test(expected = GridColumnBindingMissingException.class)
    public void shouldThrowMissingBindingException() {
        when(columnDescriptor.getOwlBinding())
                .thenReturn(Optional.empty());
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
    }
}
