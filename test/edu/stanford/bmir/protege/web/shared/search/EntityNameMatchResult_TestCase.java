package edu.stanford.bmir.protege.web.shared.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/11/2013
 */
public class EntityNameMatchResult_TestCase {


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForNegativeStart() {
        new EntityNameMatchResult(-1, 0, EntityNameMatchType.SUB_STRING_MATCH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForNegativeEnd() {
        new EntityNameMatchResult(0, -1, EntityNameMatchType.SUB_STRING_MATCH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForEndLessThanStart() {
        new EntityNameMatchResult(1, 0, EntityNameMatchType.SUB_STRING_MATCH);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullType() {
        new EntityNameMatchResult(0, 1, null);
    }

    @Test
    public void shouldAcceptStartEqualToEnd() {
        new EntityNameMatchResult(1, 1, EntityNameMatchType.SUB_STRING_MATCH);
    }

    @Test
    public void shouldReturnTrueForEqualButDifferentEntityMatchResults() {
        EntityNameMatchResult resultA = new EntityNameMatchResult(1, 2, EntityNameMatchType.SUB_STRING_MATCH);
        EntityNameMatchResult resultB = new EntityNameMatchResult(1, 2, EntityNameMatchType.SUB_STRING_MATCH);
        assertEquals(resultA, resultB);
    }

    @Test
    public void shouldReturnSameHashCodeForEqualButDifferentEntityMatchResults() {
        EntityNameMatchResult resultA = new EntityNameMatchResult(1, 2, EntityNameMatchType.SUB_STRING_MATCH);
        EntityNameMatchResult resultB = new EntityNameMatchResult(1, 2, EntityNameMatchType.SUB_STRING_MATCH);
        assertEquals(resultA.hashCode(), resultB.hashCode());
    }

    @Test
    public void shouldReturnZeroForComparisonOfEqual() {
        EntityNameMatchResult resultA = new EntityNameMatchResult(0, 1, EntityNameMatchType.WORD_MATCH);
        EntityNameMatchResult resultB = new EntityNameMatchResult(0, 1, EntityNameMatchType.WORD_MATCH);
        assertEquals(0, resultA.compareTo(resultB));
    }

    @Test
    public void shouldReturnNegativeIntegerForExactComparedToWord() {
        EntityNameMatchResult resultA = new EntityNameMatchResult(1, 2, EntityNameMatchType.EXACT_MATCH);
        EntityNameMatchResult resultB = new EntityNameMatchResult(0, 1, EntityNameMatchType.WORD_MATCH);
        assertEquals(true, resultA.compareTo(resultB) < 0);
    }


    @Test
    public void shouldReturnNegativeIntegerForWordComparedToWordPrefix() {
        EntityNameMatchResult resultA = new EntityNameMatchResult(1, 2, EntityNameMatchType.WORD_MATCH);
        EntityNameMatchResult resultB = new EntityNameMatchResult(0, 1, EntityNameMatchType.WORD_PREFIX_MATCH);
        assertEquals(true, resultA.compareTo(resultB) < 0);
    }

    @Test
    public void shouldReturnNegativeIntegerForWordPrefixComparedToSubString() {
        EntityNameMatchResult resultA = new EntityNameMatchResult(1, 2, EntityNameMatchType.WORD_PREFIX_MATCH);
        EntityNameMatchResult resultB = new EntityNameMatchResult(0, 1, EntityNameMatchType.SUB_STRING_MATCH);
        assertEquals(true, resultA.compareTo(resultB) < 0);
    }

    @Test
    public void shouldReturnNegativeIntegerForSmallerStartThanLargerStart() {
        EntityNameMatchResult resultA = new EntityNameMatchResult(0, 1, EntityNameMatchType.EXACT_MATCH);
        EntityNameMatchResult resultB = new EntityNameMatchResult(1, 1, EntityNameMatchType.EXACT_MATCH);
        assertEquals(true, resultA.compareTo(resultB) < 0);
    }

    @Test
    public void shouldReturnNegativeIntegerForSmallerEndThanLargerEnd() {
        EntityNameMatchResult resultA = new EntityNameMatchResult(1, 1, EntityNameMatchType.EXACT_MATCH);
        EntityNameMatchResult resultB = new EntityNameMatchResult(1, 2, EntityNameMatchType.EXACT_MATCH);
        assertEquals(true, resultA.compareTo(resultB) < 0);
    }
    
    


}
