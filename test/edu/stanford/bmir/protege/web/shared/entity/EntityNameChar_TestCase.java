package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.search.EntityNameCharType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/11/2013
 */
public class EntityNameChar_TestCase {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfStringIsNull() {
        EntityNameCharType.getType(null, 0);
    }

    @Test
    public void shouldReturnEscapingQuoteForSingleQuoteAtIndexZero() {
        EntityNameCharType type = EntityNameCharType.getType(0, '\'', 1);
        assertEquals(EntityNameCharType.ESCAPING_QUOTE, type);
    }

    @Test
    public void shouldReturnEscapingQuoteForSingleQuoteAtLastIndex() {
        EntityNameCharType type = EntityNameCharType.getType(1, '\'', 2);
        assertEquals(EntityNameCharType.ESCAPING_QUOTE, type);
    }

    @Test
    public void shouldReturnBoundaryForSpace() {
        EntityNameCharType type = EntityNameCharType.getType(0, ' ', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForUnderScore() {
        EntityNameCharType type = EntityNameCharType.getType(0, '_', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForComma() {
        EntityNameCharType type = EntityNameCharType.getType(0, ',', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForBackSlash() {
        EntityNameCharType type = EntityNameCharType.getType(0, '\\', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForColon() {
        EntityNameCharType type = EntityNameCharType.getType(0, ':', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForSemiColon() {
        EntityNameCharType type = EntityNameCharType.getType(0, ';', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForHash() {
        EntityNameCharType type = EntityNameCharType.getType(0, '#', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForAt() {
        EntityNameCharType type = EntityNameCharType.getType(0, '@', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForDollar() {
        EntityNameCharType type = EntityNameCharType.getType(0, '$', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForPercent() {
        EntityNameCharType type = EntityNameCharType.getType(0, '%', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForHat() {
        EntityNameCharType type = EntityNameCharType.getType(0, '^', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForAmpersand() {
        EntityNameCharType type = EntityNameCharType.getType(0, '&', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForAsterisk() {
        EntityNameCharType type = EntityNameCharType.getType(0, '*', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForPlus() {
        EntityNameCharType type = EntityNameCharType.getType(0, '+', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForEquals() {
        EntityNameCharType type = EntityNameCharType.getType(0, '=', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForOpenBracket() {
        EntityNameCharType type = EntityNameCharType.getType(0, '(', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForCloseBracket() {
        EntityNameCharType type = EntityNameCharType.getType(0, ')', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForOpenBrace() {
        EntityNameCharType type = EntityNameCharType.getType(0, '{', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForCloseBrace() {
        EntityNameCharType type = EntityNameCharType.getType(0, '}', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }
    @Test
    public void shouldReturnBoundaryForOpenSquareBracket() {
        EntityNameCharType type = EntityNameCharType.getType(0, '[', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForCloseSquareBracket() {
        EntityNameCharType type = EntityNameCharType.getType(0, ']', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }
    @Test
    public void shouldReturnBoundaryForLessThan() {
        EntityNameCharType type = EntityNameCharType.getType(0, '<', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnBoundaryForGreaterThan() {
        EntityNameCharType type = EntityNameCharType.getType(0, '>', 1);
        assertEquals(EntityNameCharType.BOUNDARY, type);
    }

    @Test
    public void shouldReturnLetterForHyphen() {
        EntityNameCharType type = EntityNameCharType.getType(0, '-', 1);
        assertEquals(EntityNameCharType.LETTER, type);
    }
}
