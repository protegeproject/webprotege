package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.entity.EntityNameUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameUtils_IsWordStart_TestCase {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullString() {
        EntityNameUtils.isWordStart(null, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForNegativeStart() {
        EntityNameUtils.isWordStart(" ", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartEqualToStringLength() {
        EntityNameUtils.isWordStart(" ", 1);
    }


    @Test
    public void shouldReturnTrueForLowerCaseCharAtZeroIndex() {
        boolean start = EntityNameUtils.isWordStart("a", 0);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForLowerCaseCharFollowingSingleQuoteAtIndexZero() {
        boolean start = EntityNameUtils.isWordStart("'a", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForUpperCaseCharAtZeroIndex() {
        boolean start = EntityNameUtils.isWordStart("A", 0);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForUpperCaseCharFollowingSingleQuoteAtIndexZero() {
        boolean start = EntityNameUtils.isWordStart("'A", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForDigitCharAtZeroIndex() {
        boolean start = EntityNameUtils.isWordStart("2", 0);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForDigitCharFollowingSingleQuoteAtIndexZero() {
        boolean start = EntityNameUtils.isWordStart("'2", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnFalseForSingleQuoteAtZeroIndex() {
        boolean start = EntityNameUtils.isWordStart("'a", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForSingleQuoteAtEndIndex() {
        boolean start = EntityNameUtils.isWordStart("a'", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForDigitThatFollowsDigit() {
        boolean start = EntityNameUtils.isWordStart("22", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnTrueForUpperCaseUpperCaseLowerCaseAtIndexOne() {
        boolean start = EntityNameUtils.isWordStart("AAa", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnFalseForLowerCaseLetterAfterUpperCaseLetter() {
        boolean start = EntityNameUtils.isWordStart("Aa", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForUpperCaseUpperCaseAtIndexOne() {
        boolean start = EntityNameUtils.isWordStart("AA", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnTrueForUpperCaseDigitAtIndexOne() {
        boolean start = EntityNameUtils.isWordStart("A2", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnFalseForLowerCaseCharPrecededByLowerCaseChar() {
        boolean start = EntityNameUtils.isWordStart("abc", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForSpace() {
        boolean start = EntityNameUtils.isWordStart(" ", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForUnderscore() {
        boolean start = EntityNameUtils.isWordStart("_", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForBackSlash() {
        boolean start = EntityNameUtils.isWordStart("\\", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForOpenBracket() {
        boolean start = EntityNameUtils.isWordStart("(", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForCloseBracket() {
        boolean start = EntityNameUtils.isWordStart(")", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForOpenBrace() {
        boolean start = EntityNameUtils.isWordStart("{", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForCloseBrace() {
        boolean start = EntityNameUtils.isWordStart("}", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForOpenSquareBracket() {
        boolean start = EntityNameUtils.isWordStart("[", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForCloseSquareBracket() {
        boolean start = EntityNameUtils.isWordStart("]", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForLessThan() {
        boolean start = EntityNameUtils.isWordStart("<", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForGreaterThan() {
        boolean start = EntityNameUtils.isWordStart(">", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForComma() {
        boolean start = EntityNameUtils.isWordStart(",", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForHyphenFollowingLetter() {
        boolean start = EntityNameUtils.isWordStart("x-", 1);
        assertEquals(false, start);
    }

}
