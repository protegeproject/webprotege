package edu.stanford.bmir.protege.web.server.shortform;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

public class ShortFormMatchPosition_TestCase {

    @Test
    public void shouldCompareLessThanOnStart() {
        var p1 = ShortFormMatchPosition.get(1, 4);
        var p2 = ShortFormMatchPosition.get(2, 4);
        assertThat(p1.compareTo(p2), lessThan(0));
    }

    @Test
    public void shouldCompareLessThanOnEnd() {
        var p1 = ShortFormMatchPosition.get(1, 3);
        var p2 = ShortFormMatchPosition.get(1, 4);
        assertThat(p1.compareTo(p2), lessThan(0));
    }

    @Test
    public void shouldCompareEqual() {
        var p1 = ShortFormMatchPosition.get(1, 3);
        var p2 = ShortFormMatchPosition.get(1, 3);
        assertThat(p1.compareTo(p2), equalTo(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForStartGreaterThanEnd() {
        ShortFormMatchPosition.get(3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForStartLessThanZero() {
        ShortFormMatchPosition.get(-2, 2);
    }
}