package edu.stanford.bmir.protege.web.shared.search;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameWordFinder_IndexOfWordTestCase {


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullString() {
        EntityNameWordFinder.indexOfWord(null, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartLessThanZero() {
        EntityNameWordFinder.indexOfWord("x", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartEqualToStringLength() {
        EntityNameWordFinder.indexOfWord("x", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartGreaterThanStringLength() {
        EntityNameWordFinder.indexOfWord("x", 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForEmptyString() {
        EntityNameWordFinder.indexOfWord("", 0);
    }

    @Test
    public void shouldReturnZeroForNonQuotedStringAndStartIndexOfZero() {
        int position = EntityNameWordFinder.indexOfWord("xyz", 0);
        assertEquals(0, position);
    }

    @Test
    public void shouldSkipSingleQuoteAtStart() {
        int position = EntityNameWordFinder.indexOfWord("'xyz'", 0);
        assertEquals(1, position);
    }

    @Test
    public void shouldIgnoreSingleQuoteInMiddleOfString() {
        int position = EntityNameWordFinder.indexOfWord("x'y c", 1);
        assertEquals(4, position);
    }

    @Test
    public void shouldFindNextUpperCaseLetter() {
        int position = EntityNameWordFinder.indexOfWord("xyzXyz", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindFinalUpperCaseLetter() {
        int position = EntityNameWordFinder.indexOfWord("xyzX", 2);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNumberStart() {
        int position = EntityNameWordFinder.indexOfWord("xx200", 1);
        assertEquals(position, 2);
    }


    @Test
    public void shouldFindFirstCharacterAfterNumber() {
        int position = EntityNameWordFinder.indexOfWord("200x", 1);
        assertEquals(position, 3);
    }

    @Test
    public void shouldSkipOverHypens() {
        int position = EntityNameWordFinder.indexOfWord("z-xA", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindLastUpperCaseLetterInUpperCaseLetterRun() {
        int position = EntityNameWordFinder.indexOfWord("XXXyz", 1);
        assertEquals(2, position);
    }

    @Test
    public void shouldFindLastUpperCaseLetterInUpperCaseLetterRunWithHypen() {
        int position = EntityNameWordFinder.indexOfWord("XXX-z", 1);
        assertEquals(2, position);
    }

    @Test
    public void shouldFindNextCharacterAfterWhiteSpace() {
        int position = EntityNameWordFinder.indexOfWord("xx x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterBackSlash() {
        int position = EntityNameWordFinder.indexOfWord("xx\\x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenBracket() {
        int position = EntityNameWordFinder.indexOfWord("xx(x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseBracket() {
        int position = EntityNameWordFinder.indexOfWord("xx)x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenBrace() {
        int position = EntityNameWordFinder.indexOfWord("xx{x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseBrace() {
        int position = EntityNameWordFinder.indexOfWord("xx}x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenSquareBracket() {
        int position = EntityNameWordFinder.indexOfWord("xx[x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseSquareBracket() {
        int position = EntityNameWordFinder.indexOfWord("xx]x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterLessThan() {
        int position = EntityNameWordFinder.indexOfWord("xx<x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterGreaterThan() {
        int position = EntityNameWordFinder.indexOfWord("xx>x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterColon() {
        int position = EntityNameWordFinder.indexOfWord("xx:x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterHash() {
        int position = EntityNameWordFinder.indexOfWord("xx#x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnMinusOneIfAfterLastWordStartIndex() {
        int position = EntityNameWordFinder.indexOfWord("xyz", 1);
        assertEquals(-1, position);
    }

}
