package edu.stanford.bmir.protege.web.shared.pagination;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/10/2013
 */
public class PageRequestTestCase {

    @Test
    public void requestSinglePageDoesNotReturnNull() {
        assertNotNull(PageRequest.requestSinglePage());
    }

    @Test
    public void requestSinglePageReturnsRequestWithPageNumberOfOneAndMaxSize() {
        PageRequest request = PageRequest.requestSinglePage();
        assertTrue(1 == request.getPageNumber());
        assertTrue(PageRequest.MAX_PAGE_SIZE == request.getPageSize());
    }

    @Test
    public void requestFirstPageDoesNotReturnNull() {
        assertNotNull(PageRequest.requestFirstPage());
    }

    @Test
    public void getFirstPageReturnsRequestWithAPageNumberOfOneAndDefaultPageSize() {
        PageRequest request = PageRequest.requestFirstPage();
        assertTrue(1 == request.getPageNumber());
        assertTrue(PageRequest.DEFAULT_PAGE_SIZE == request.getPageSize());
    }

    @Test
    public void requestPageDoesNotReturnNull() {
        assertNotNull(PageRequest.requestPage(2));
    }

    @Test
    public void requestPageWithSizeDoesNotReturnNull() {
        assertNotNull(PageRequest.requestPageWithSize(2, 10));
    }

    @Test
    public void requestPageReturnsRequestWithSpecifiedPageNumberAndDefaultSize() {
        PageRequest request = PageRequest.requestPage(2);
        assertTrue(2 == request.getPageNumber());
        assertTrue(PageRequest.DEFAULT_PAGE_SIZE == request.getPageSize());
    }

    @Test
    public void requestPageWithSizeReturnsRequestWithSpecifiedPageAndSize() {
        PageRequest request = PageRequest.requestPageWithSize(2, 10);
        assertTrue(2 == request.getPageNumber());
        assertTrue(10 == request.getPageSize());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void requestPageWithPageNumberOfZeroThrowsIndexOutOfBoundsException() {
        PageRequest.requestPage(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void requestPageWithNegativePageNumberThrowsIndexOutOfBoundsException() {
        PageRequest.requestPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestPageWithZeroPageSizeThrowsIllegalArgumentException() {
        PageRequest.requestPageWithSize(1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestPageWithNegativePageSizeThrowsIllegalArgumentException() {
        PageRequest.requestPageWithSize(1, -1);
    }

    @Test
    public void equalPageNumberAndSizeAreEqualRequestsWithTheSameHashCode() {
        PageRequest requestA = PageRequest.requestPageWithSize(3, 10);
        PageRequest requestB = PageRequest.requestPageWithSize(3, 10);
        assertEquals(requestA, requestB);
        assertEquals(requestA.hashCode(), requestB.hashCode());
    }

}
