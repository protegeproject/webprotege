package edu.stanford.bmir.protege.web.shared.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameWordFinder_IsWordStartTestCase {

    @Test
    public void shouldReturnTrueForLowerCaseCharAtZeroIndex() {
        boolean start = EntityNameWordFinder.isWordStart("a", 0);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForLowerCaseCharFollowingSingleQuoteAtIndexZero() {
        boolean start = EntityNameWordFinder.isWordStart("'a", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForUpperCaseCharAtZeroIndex() {
        boolean start = EntityNameWordFinder.isWordStart("A", 0);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForUpperCaseCharFollowingSingleQuoteAtIndexZero() {
        boolean start = EntityNameWordFinder.isWordStart("'A", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForDigitCharAtZeroIndex() {
        boolean start = EntityNameWordFinder.isWordStart("2", 0);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnTrueForDigitCharFollowingSingleQuoteAtIndexZero() {
        boolean start = EntityNameWordFinder.isWordStart("'2", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnFalseForSingleQuoteAtZeroIndex() {
        boolean start = EntityNameWordFinder.isWordStart("'a", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForSingleQuoteAtEndIndex() {
        boolean start = EntityNameWordFinder.isWordStart("a'", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForDigitThatFollowsDigit() {
        boolean start = EntityNameWordFinder.isWordStart("22", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnTrueForUpperCaseUpperCaseLowerCaseAtIndexOne() {
        boolean start = EntityNameWordFinder.isWordStart("AAa", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnFalseForLowerCaseLetterAfterUpperCaseLetter() {
        boolean start = EntityNameWordFinder.isWordStart("Aa", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForUpperCaseUpperCaseAtIndexOne() {
        boolean start = EntityNameWordFinder.isWordStart("AA", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnTrueForUpperCaseDigitAtIndexOne() {
        boolean start = EntityNameWordFinder.isWordStart("A2", 1);
        assertEquals(true, start);
    }

    @Test
    public void shouldReturnFalseForLowerCaseCharPrecededByLowerCaseChar() {
        boolean start = EntityNameWordFinder.isWordStart("abc", 1);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForSpace() {
        boolean start = EntityNameWordFinder.isWordStart(" ", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForUnderscore() {
        boolean start = EntityNameWordFinder.isWordStart("_", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForBackSlash() {
        boolean start = EntityNameWordFinder.isWordStart("\\", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForOpenBracket() {
        boolean start = EntityNameWordFinder.isWordStart("(", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForCloseBracket() {
        boolean start = EntityNameWordFinder.isWordStart(")", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForOpenBrace() {
        boolean start = EntityNameWordFinder.isWordStart("{", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForCloseBrace() {
        boolean start = EntityNameWordFinder.isWordStart("}", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForOpenSquareBracket() {
        boolean start = EntityNameWordFinder.isWordStart("[", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForCloseSquareBracket() {
        boolean start = EntityNameWordFinder.isWordStart("]", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForLessThan() {
        boolean start = EntityNameWordFinder.isWordStart("<", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForGreaterThan() {
        boolean start = EntityNameWordFinder.isWordStart(">", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForComma() {
        boolean start = EntityNameWordFinder.isWordStart(",", 0);
        assertEquals(false, start);
    }

    @Test
    public void shouldReturnFalseForHyphenFollowingLetter() {
        boolean start = EntityNameWordFinder.isWordStart("x-", 1);
        assertEquals(false, start);
    }

}
