package edu.stanford.bmir.protege.web.shared.search;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameWordFinderTestCase {


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullString() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        finder.findNextWord(null, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartLessThanZero() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        finder.findNextWord("x", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartEqualToStringLength() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        finder.findNextWord("x", -1);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForStartGreaterThanStringLength() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        finder.findNextWord("x", 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowIndexOutOfBoundsExceptionForEmptyString() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        finder.findNextWord("", 0);
    }

    @Test
    public void shouldReturnZeroForNonQuotedStringAndStartIndexOfZero() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xyz", 0);
        assertEquals(0, position);
    }

    @Test
    public void shouldSkipSingleQuoteAtStart() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("'xyz'", 0);
        assertEquals(1, position);
    }

    @Test
    public void shouldFindNextUpperCaseLetter() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xyzXyz", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindFinalUpperCaseLetter() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xyzX", 2);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNumberStart() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx200", 1);
        assertEquals(position, 2);
    }


    @Test
    public void shouldFindFirstCharacterAfterNumber() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("200x", 1);
        assertEquals(position, 3);
    }

    @Test
    public void shouldSkipOverHypens() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("z-xA", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindLastUpperCaseLetterInUpperCaseLetterRun() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("XXXyz", 1);
        assertEquals(2, position);
    }

    @Test
    public void shouldFindLastUpperCaseLetterInUpperCaseLetterRunWithHypen() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("XXX-z", 1);
        assertEquals(2, position);
    }

    @Test
    public void shouldFindNextCharacterAfterWhiteSpace() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterBackSlash() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx\\x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenBracket() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx(x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseBracket() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx)x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenBrace() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx{x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseBrace() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx}x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterOpenSquareBracket() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx[x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterCloseSquareBracket() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx]x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterLessThan() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx<x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterGreaterThan() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx>x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterColon() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx:x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldFindNextCharacterAfterHash() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx#x", 1);
        assertEquals(3, position);
    }

    @Test
    public void shouldSkipMultipleBoundaryCharacters() {
        EntityNameWordFinder finder = new EntityNameWordFinder();
        int position = finder.findNextWord("xx(( p", 1);
        assertEquals(5, position);
    }
}
