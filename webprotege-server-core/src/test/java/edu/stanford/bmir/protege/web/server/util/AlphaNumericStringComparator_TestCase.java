package edu.stanford.bmir.protege.web.server.util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Apr 2018
 */
public class AlphaNumericStringComparator_TestCase {

    private AlphaNumericStringComparator comparator;

    @Before
    public void setUp() {
        comparator = new AlphaNumericStringComparator();
    }

    @Test
    public void shouldCompareTwoEmptyStrings() {
        assertThat(comparator.compare("", ""), is(0));
    }

    @Test
    public void shouldCompareEmptyStringWithNonEmptyString() {
        assertThat(comparator.compare("", "a"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareNonEmptyStringWithEmptyString() {
        assertThat(comparator.compare("a", ""), is(greaterThan(0)));
    }

    @Test
    public void shouldCompareEqualStrings() {
        assertThat(comparator.compare("a", "a"), is(0));
    }

    @Test
    public void shouldCompareStringsInNaturalOrder() {
        assertThat(comparator.compare("a", "b"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareStringsInNaturalOrder_reverse() {
        assertThat(comparator.compare("b", "a"), is(greaterThan(0)));
    }

    @Test
    public void shouldCompareStringsInCaseInsensitiveNaturalOrder() {
        assertThat(comparator.compare("a", "B"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareStringsInCaseInsensitiveNaturalOrder_reverse() {
        assertThat(comparator.compare("B", "a"), is(greaterThan(0)));
    }
    
    @Test
    public void shouldCompareDigitsInValueOrder() {
        assertThat(comparator.compare("50", "100"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareDigitsInValueOrder_reverse() {
        assertThat(comparator.compare("100", "50"), is(greaterThan(0)));
    }

    @Test
    public void shouldCompareAlphaNumericStrings() {
        assertThat(comparator.compare("A50", "A100"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareAlphaNumericStrings_reverse() {
        assertThat(comparator.compare("A100", "A50"), is(greaterThan(0)));
    }

    @Test
    public void shouldCompareAlphaNumericAlternatingStrings() {
        assertThat(comparator.compare("A10B50", "A10B100"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareAlphaNumericAlternatingStrings_reverse() {
        assertThat(comparator.compare("A10B50", "A10B100"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareUnevenStrings() {
        assertThat(comparator.compare("A10B50", "A10B50C"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareUnevenStrings_reverse() {
        assertThat(comparator.compare("A10B50C", "A10B50"), is(greaterThan(0)));
    }

    @Test
    public void shouldCompareStringsWithZeroPadding() {
        assertThat(comparator.compare("A000050", "A000051"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareStringsWithUnevenPadding() {
        assertThat(comparator.compare("A0050", "A0000000050"), is(lessThan(0)));
    }

    @Test
    public void shouldCompareStringsNumericallyBeforeLength() {
        assertThat(comparator.compare("3B26 X Y", "3B29 X"), is(lessThan(0)));
    }
}
