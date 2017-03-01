package edu.stanford.bmir.protege.web.shared.pagination;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/09/2013
 */
public class PageTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfPageNumberIsGreaterThanPageCount() {
        new Page<String>(2, 1, Collections.emptyList(), 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIndexOfOutBoundsExceptionForPageNumberOfZero() {
        new Page<String>(0, 1, Collections.emptyList(), 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionForPageCountOfZero() {
        new Page<String>(1, 0, Collections.emptyList(), 100);
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerExceptionForNullElements() {
        new Page<String>(1, 1, null, 100);
    }

    @Test
    public void getElementsReturnsCopy() {
        List<String> suppliedElements = Arrays.asList("A");
        Page<String> p = new Page(1, 1, suppliedElements, 100);
        List<String> elements = p.getPageElements();
        elements.clear();
        assertEquals(suppliedElements, p.getPageElements());
    }

    @Test
    public void getPageNumberReturnsSuppliedPageNumber() {
        Page<String> p = new Page<String>(2, 2, Collections.emptyList(), 100);
        assertEquals(2, p.getPageNumber());
    }
}
