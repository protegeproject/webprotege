package edu.stanford.bmir.protege.web.client.form.input;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckBoxPresenter_TestCase {

    private CheckBoxPresenter presenter;

    @Mock
    private CheckBoxView view;

    @Mock
    private AcceptsOneWidget container;

    @Before
    public void setUp() throws Exception {
        presenter = new CheckBoxPresenter(view);
        presenter.start(container);
    }

    @Test
    public void shouldSetViewOnStart() {
        verify(container, times(1)).setWidget(view);
    }



    @Test
    public void shouldSetEnabled() {
        presenter.setEnabled(true);
        verify(view, times(1)).setEnabled(true);
    }

    @Test
    public void shouldSetReadOnly() {
        presenter.setReadOnly(true);
        verify(view, times(1)).setReadOnly(true);
    }

    @Test
    public void shouldSetValue() {
        presenter.setValue(true);
        verify(view, times(1)).setValue(true);
    }

    @Test
    public void shouldSetValueAndFireEvents() {
        presenter.setValue(true, true);
        verify(view, times(1)).setValue(true, true);
    }

    @Test
    public void shouldSetText() {
        presenter.setText("Hello");
        verify(view, times(1)).setText("Hello");
    }

    @Test
    public void shouldToggleValueFromFalseTrueWhenClicked() {
        when(view.isEnabled()).thenReturn(true);
        when(view.isReadOnly()).thenReturn(false);
        when(view.getValue()).thenReturn(false);
        ArgumentCaptor<MouseUpHandler> handler = ArgumentCaptor.forClass(MouseUpHandler.class);
        verify(view).addMouseUpHandler(handler.capture());
        handler.getValue().onMouseUp(mock(MouseUpEvent.class));
        verify(view).setValue(true);
    }

    @Test
    public void shouldToggleValueFromTrueToFalseWhenClicked() {
        when(view.isEnabled()).thenReturn(true);
        when(view.isReadOnly()).thenReturn(false);
        when(view.getValue()).thenReturn(true);
        ArgumentCaptor<MouseUpHandler> handler = ArgumentCaptor.forClass(MouseUpHandler.class);
        verify(view).addMouseUpHandler(handler.capture());
        handler.getValue().onMouseUp(mock(MouseUpEvent.class));
        verify(view).setValue(false);
    }

    @Test
    public void shouldNotToggleValueWhenNotEnabled() {
        when(view.isEnabled()).thenReturn(false);
        ArgumentCaptor<MouseUpHandler> handler = ArgumentCaptor.forClass(MouseUpHandler.class);
        verify(view).addMouseUpHandler(handler.capture());
        handler.getValue().onMouseUp(mock(MouseUpEvent.class));
        verify(view, never()).setValue(any());
    }

    @Test
    public void shouldNotToggleValueWhenReadOnly() {
        when(view.isReadOnly()).thenReturn(false);
        ArgumentCaptor<MouseUpHandler> handler = ArgumentCaptor.forClass(MouseUpHandler.class);
        verify(view).addMouseUpHandler(handler.capture());
        handler.getValue().onMouseUp(mock(MouseUpEvent.class));
        verify(view, never()).setValue(any());
    }
}