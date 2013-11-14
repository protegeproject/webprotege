package edu.stanford.bmir.protege.web.shared.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameUtils_IndexOfWordEnd_TestCase {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullString() {
        EntityNameUtils.indexOfWordEnd(null, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartLessThanZero() {
        EntityNameUtils.indexOfWordEnd("x", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartEqualToStringLength() {
        EntityNameUtils.indexOfWordEnd("x", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartGreaterThanStringLength() {
        EntityNameUtils.indexOfWordEnd("x", 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForEmptyString() {
        EntityNameUtils.indexOfWordEnd("", 0);
    }


    @Test
    public void shouldReturnEndOfSingleCharacterString() {
        int position = EntityNameUtils.indexOfWordEnd("a", 0);
        assertEquals(1, position);
    }

    @Test
    public void shouldReturnEndOfCurrentWord() {
        int position = EntityNameUtils.indexOfWordEnd("abc", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfCurrentWordInQuotes() {
        int position = EntityNameUtils.indexOfWordEnd("'ab'", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldSkipSingleQuoteInMiddleOfString() {
        int position = EntityNameUtils.indexOfWordEnd("a'b", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfDigitRunEndedByUpperCaseLetter() {
        int position = EntityNameUtils.indexOfWordEnd("200A", 0);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfDigitRunEndedByLowerCaseLetter() {
        int position = EntityNameUtils.indexOfWordEnd("200a", 0);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfLowerCaseRun() {
        int position = EntityNameUtils.indexOfWordEnd("abcDe", 0);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfCurrentCamelCaseWord() {
        int position = EntityNameUtils.indexOfWordEnd("Abc", 0);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnLastIndexOfUpperCaseCamelCaseRun() {
        int position = EntityNameUtils.indexOfWordEnd("AAAb", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeSpace() {
        int position = EntityNameUtils.indexOfWordEnd("aa a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeOpenBracket() {
        int position = EntityNameUtils.indexOfWordEnd("aa(a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeCloseBracket() {
        int position = EntityNameUtils.indexOfWordEnd("aa)a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeOpenBrace() {
        int position = EntityNameUtils.indexOfWordEnd("aa{a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeCloseBrace() {
        int position = EntityNameUtils.indexOfWordEnd("aa}a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeOpenSquareBracket() {
        int position = EntityNameUtils.indexOfWordEnd("aa[a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeCloseSquareBracket() {
        int position = EntityNameUtils.indexOfWordEnd("aa]a", 0);
        assertEquals(2, position);
    }


    @Test
    public void shouldReturnLastIndexBeforeLessThan() {
        int position = EntityNameUtils.indexOfWordEnd("aa<a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeGreaterThan() {
        int position = EntityNameUtils.indexOfWordEnd("aa>a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeUnderScore() {
        int position = EntityNameUtils.indexOfWordEnd("aa_a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeComma() {
        int position = EntityNameUtils.indexOfWordEnd("aa,a", 0);
        assertEquals(2, position);
    }
}
