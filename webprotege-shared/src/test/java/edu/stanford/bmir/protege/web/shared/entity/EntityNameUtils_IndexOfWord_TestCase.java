package edu.stanford.bmir.protege.web.shared.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameUtils_IndexOfWord_TestCase {


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullString() {
        EntityNameUtils.indexOfWord(null, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartLessThanZero() {
        EntityNameUtils.indexOfWord("x", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartEqualToStringLength() {
        EntityNameUtils.indexOfWord("x", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartGreaterThanStringLength() {
        EntityNameUtils.indexOfWord("x", 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForEmptyString() {
        EntityNameUtils.indexOfWord("", 0);
    }

    @Test
    public void shouldReturnZeroForNonQuotedStringAndStartIndexOfZero() {
        int position = EntityNameUtils.indexOfWord("xyz", 0);
        assertEquals(0, position);
    }

    @Test
    public void shouldSkipSingleQuoteAtStart() {
        int position = EntityNameUtils.indexOfWord("'xyz'", 0);
        assertEquals(1, position);
    }

    @Test
    public void shouldIgnoreSingleQuoteInMiddleOfString() {
        int position = EntityNameUtils.indexOfWord("x'y c", 1);
        assertEquals(4, position);
    }

    @Test
    public void shouldFindNextUpperCaseLetter() {
        int position = EntityNameUtils.indexOfWord("xyzXyz", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindFinalUpperCaseLetter() {
        int position = EntityNameUtils.indexOfWord("xyzX", 2);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNumberStart() {
        int position = EntityNameUtils.indexOfWord("xx200", 1);
        assertEquals(position, 2);
    }


    @Test
    public void shouldFindFirstCharacterAfterNumber() {
        int position = EntityNameUtils.indexOfWord("200x", 1);
        assertEquals(position, 3);
    }

    @Test
    public void shouldSkipOverHypens() {
        int position = EntityNameUtils.indexOfWord("z-xA", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindLastUpperCaseLetterInUpperCaseLetterRun() {
        int position = EntityNameUtils.indexOfWord("XXXyz", 1);
        assertEquals(2, position);
    }

    @Test
    public void shouldFindLastUpperCaseLetterInUpperCaseLetterRunWithHypen() {
        int position = EntityNameUtils.indexOfWord("XXX-z", 1);
        assertEquals(2, position);
    }

    @Test
    public void shouldFindNextCharacterAfterWhiteSpace() {
        int position = EntityNameUtils.indexOfWord("xx x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterBackSlash() {
        int position = EntityNameUtils.indexOfWord("xx\\x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenBracket() {
        int position = EntityNameUtils.indexOfWord("xx(x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseBracket() {
        int position = EntityNameUtils.indexOfWord("xx)x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenBrace() {
        int position = EntityNameUtils.indexOfWord("xx{x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseBrace() {
        int position = EntityNameUtils.indexOfWord("xx}x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenSquareBracket() {
        int position = EntityNameUtils.indexOfWord("xx[x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseSquareBracket() {
        int position = EntityNameUtils.indexOfWord("xx]x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterLessThan() {
        int position = EntityNameUtils.indexOfWord("xx<x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterGreaterThan() {
        int position = EntityNameUtils.indexOfWord("xx>x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterColon() {
        int position = EntityNameUtils.indexOfWord("xx:x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterHash() {
        int position = EntityNameUtils.indexOfWord("xx#x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldReturnMinusOneIfAfterLastWordStartIndex() {
        int position = EntityNameUtils.indexOfWord("xyz", 1);
        assertEquals(-1, position);
    }

}
