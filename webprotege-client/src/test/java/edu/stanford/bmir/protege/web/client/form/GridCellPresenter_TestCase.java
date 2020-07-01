package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellData;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GridCellPresenter_TestCase {

    private GridCellPresenter presenter;

    @Mock
    private GridCellView view;

    @Mock
    private GridColumnDescriptorDto columnDescriptor;

    @Mock
    private FormControlStackPresenter formControlStackPresenter;

    @Mock
    private AcceptsOneWidget container;

    @Mock
    private GridColumnId columnId;

    @Mock
    private FormControlData formControlValue;

    @Mock
    private GridCellDataDto cellData;

    @Mock
    private Page<FormControlDataDto> cellDataPage;

    @Mock
    private GridColumnVisibilityManager columnVisibilityManager;

    @Before
    public void setUp() throws Exception {
        when(cellData.getValues())
                .thenReturn(cellDataPage);
        when(columnDescriptor.getId())
                .thenReturn(columnId);
        when(formControlStackPresenter.getValue())
                .thenReturn(ImmutableList.of(formControlValue));
        presenter = new GridCellPresenter(view,
                                          columnDescriptor,
                                          formControlStackPresenter);
    }

    @Test
    public void shouldSetViewInContainer() {
        presenter.start(container);
        verify(container).setWidget(any());
    }

    @Test
    public void shouldDisableControlStack() {
        presenter.setEnabled(false);
        verify(formControlStackPresenter).setEnabled(false);
    }

    @Test
    public void shouldClearControlStack() {
        presenter.clear();
        verify(formControlStackPresenter).clearValue();
    }

    @Test
    public void shouldRequestFocusOnControlStack() {
        presenter.requestFocus();
        verify(formControlStackPresenter).requestFocus();
    }

    @Test
    public void shouldReturnGridColumnId() {
        GridColumnId id = presenter.getId();
        assertThat(id, is(columnId));
    }

    @Test
    public void shouldReturnFormStackControlValue() {
        GridCellData value = presenter.getValue();
        assertThat(value.getValues().getPageElements(), hasItem(formControlValue));
    }

    @Test
    public void shouldSetFormControlValue() {
        presenter.setValue(cellData);
        verify(formControlStackPresenter).setValue(cellDataPage);
    }
}