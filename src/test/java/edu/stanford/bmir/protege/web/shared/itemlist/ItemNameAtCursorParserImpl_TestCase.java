package edu.stanford.bmir.protege.web.shared.itemlist;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public class ItemNameAtCursorParserImpl_TestCase {

    private ItemNameAtCursorParserImpl parser;

    @Before
    public void setUp() throws Exception {
        parser = new ItemNameAtCursorParserImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForNegativeCursorPosition() {
        parser.parseItemNameAtCursor("A", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForCursorOutOfRange() {
        parser.parseItemNameAtCursor("A", 2);
    }

    @Test
    public void shouldReturnEmptyStringForEmptyString() {
        assertThat(parser.parseItemNameAtCursor("", 0), isEmptyString());
    }

    @Test
    public void shouldReturnStringBeforeCursor() {
        assertThat(parser.parseItemNameAtCursor("A", 1), is("A"));
    }

    @Test
    public void shouldReturnEmptyStringFromStart() {
        assertThat(parser.parseItemNameAtCursor("A", 0), isEmptyString());
    }

    @Test
    public void shouldReturnStringBeforeCursorAndNotFollowingNewLine() {
        assertThat(parser.parseItemNameAtCursor("A\n", 1), is("A"));
    }

    @Test
    public void shouldReturnStringBeforeCursorButNotNewLinePrefix() {
        assertThat(parser.parseItemNameAtCursor("\nA", 2), is("A"));
    }

    @Test
    public void shouldReturnStringBeforeCursorButNotPrecedingNewLine() {
        assertThat(parser.parseItemNameAtCursor("A\nB", 3), is("B"));
    }

    @Test
    public void shouldReturnEmptyStringAfterNewLine() {
        assertThat(parser.parseItemNameAtCursor("A\n", 2), is(""));
    }

    @Test
    public void shouldReturnMiddleString() {
        assertThat(parser.parseItemNameAtCursor("A\nB\nC", 3), is("B"));
    }
}
