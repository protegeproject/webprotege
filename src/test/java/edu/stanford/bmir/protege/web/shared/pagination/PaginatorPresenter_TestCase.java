package edu.stanford.bmir.protege.web.shared.pagination;

import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class PaginatorPresenter_TestCase {

    private PaginatorPresenter presenter;

    @Mock
    private PaginatorView view;

    @Before
    public void setUp() {
        presenter = new PaginatorPresenter(view);
    }

    @Test
    public void shouldReturnSuppliedView() {
        assertThat(presenter.getView(), is(view));
    }

    @Test
    public void shouldBeSetToPage1ByDefault() {
        verify(view, times(1)).setPageNumber("1");
        assertThat(presenter.getPageNumber(), is(1));
    }

    @Test
    public void shouldRejectPageNumberLessThanZero() {
        verify(view, times(1)).setPageNumber("1");
        presenter.setPageNumber(0);
        assertThat(presenter.getPageNumber(), is(1));
    }

    @Test
    public void shouldIncrementPageNumber() {
        presenter.setPageCount(2);
        presenter.setPageNumber(2);
        assertThat(presenter.getPageNumber(), is(2));
    }

    @Test
    public void shouldNotIncrementPageNumberAbovePageCount() {
        presenter.setPageCount(2);
        presenter.setPageNumber(3);
        assertThat(presenter.getPageNumber(), is(1));
    }
}
