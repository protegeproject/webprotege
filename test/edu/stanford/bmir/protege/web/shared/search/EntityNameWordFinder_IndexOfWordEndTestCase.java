package edu.stanford.bmir.protege.web.shared.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameWordFinder_IndexOfWordEndTestCase {

    @Test
    public void shouldReturnEndOfSingleCharacterString() {
        int position = EntityNameWordFinder.indexOfWordEnd("a", 0);
        assertEquals(1, position);
    }

    @Test
    public void shouldReturnEndOfCurrentWord() {
        int position = EntityNameWordFinder.indexOfWordEnd("abc", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfCurrentWordInQuotes() {
        int position = EntityNameWordFinder.indexOfWordEnd("'ab'", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldSkipSingleQuoteInMiddleOfString() {
        int position = EntityNameWordFinder.indexOfWordEnd("a'b", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfDigitRunEndedByUpperCaseLetter() {
        int position = EntityNameWordFinder.indexOfWordEnd("200A", 0);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfDigitRunEndedByLowerCaseLetter() {
        int position = EntityNameWordFinder.indexOfWordEnd("200a", 0);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfLowerCaseRun() {
        int position = EntityNameWordFinder.indexOfWordEnd("abcDe", 0);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnEndOfCurrentCamelCaseWord() {
        int position = EntityNameWordFinder.indexOfWordEnd("Abc", 0);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnLastIndexOfUpperCaseCamelCaseRun() {
        int position = EntityNameWordFinder.indexOfWordEnd("AAAb", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeSpace() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeOpenBracket() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa(a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeCloseBracket() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa)a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeOpenBrace() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa{a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeCloseBrace() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa}a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeOpenSquareBracket() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa[a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeCloseSquareBracket() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa]a", 0);
        assertEquals(2, position);
    }


    @Test
    public void shouldReturnLastIndexBeforeLessThan() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa<a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeGreaterThan() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa>a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeUnderScore() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa_a", 0);
        assertEquals(2, position);
    }

    @Test
    public void shouldReturnLastIndexBeforeComma() {
        int position = EntityNameWordFinder.indexOfWordEnd("aa,a", 0);
        assertEquals(2, position);
    }
}
