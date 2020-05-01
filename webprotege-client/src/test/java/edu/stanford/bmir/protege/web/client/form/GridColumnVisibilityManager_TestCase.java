package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-01
 */
@RunWith(MockitoJUnitRunner.class)
public class GridColumnVisibilityManager_TestCase {

    private GridColumnVisibilityManager visibilityManager;

    @Mock
    private GridColumnId columnA, columnB;

    @Mock
    private GridColumnVisibilityManager.VisibleColumnsChangedHandler visibilityChangedHandler;

    private HandlerRegistration handlerRegistration;

    @Before
    public void setUp() {
        visibilityManager = new GridColumnVisibilityManager();
        handlerRegistration = visibilityManager.addVisibleColumnsChangedHandler(visibilityChangedHandler);
    }

    @Test
    public void shouldNotBeVisibleByDefault() {
        assertThat(visibilityManager.isVisible(columnA), is(false));
    }

    @Test
    public void shouldMakeColumnsVisible() {
        visibilityManager.setVisibleColumns(ImmutableList.of(columnA, columnB));
        assertThat(visibilityManager.isVisible(columnA), is(true));
        assertThat(visibilityManager.isVisible(columnB), is(true));
    }

    @Test
    public void shouldAddHandler() {
        visibilityManager.setVisibleColumns(ImmutableList.of(columnA));
        verify(visibilityChangedHandler, times(1))
                .handleVisibleColumnsChanged();
    }

    @Test
    public void shouldNotAddHandlerTwice() {
        // Handler a second time
        visibilityManager.addVisibleColumnsChangedHandler(visibilityChangedHandler);
        visibilityManager.setVisibleColumns(ImmutableList.of(columnA));
        // Handler should only be called once
        verify(visibilityChangedHandler, times(1))
                .handleVisibleColumnsChanged();
    }

    @Test
    public void shouldNotFireChangeWhenVisibilityDoesNotChange() {
        visibilityManager.setVisibleColumns(ImmutableList.of(columnA));
        visibilityManager.setVisibleColumns(ImmutableList.of(columnA));
        verify(visibilityChangedHandler, times(1))
                .handleVisibleColumnsChanged();
    }

    @Test
    public void shouldRemoveHandler() {
        handlerRegistration.removeHandler();
        visibilityManager.setVisibleColumns(ImmutableList.of(columnA));
        verify(visibilityChangedHandler, never())
                .handleVisibleColumnsChanged();
    }
}
