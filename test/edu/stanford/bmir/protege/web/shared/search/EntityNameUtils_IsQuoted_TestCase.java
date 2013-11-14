package edu.stanford.bmir.protege.web.shared.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/11/2013
 */
public class EntityNameUtils_IsQuoted_TestCase {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfStringIsNull() {
        EntityNameUtils.isQuoted(null);
    }

    @Test
    public void shouldReturnFalseForEmptyString() {
        boolean quoted = EntityNameUtils.isQuoted("");
        assertEquals(false, quoted);
    }

    @Test
    public void shouldReturnFalseForSingleSingleQuote() {
        boolean quoted = EntityNameUtils.isQuoted("'");
        assertEquals(false, quoted);
    }


    @Test
    public void shouldReturnFalseForSingleQuoteOnlyAtStart() {
        boolean quoted = EntityNameUtils.isQuoted("'a");
        assertEquals(false, quoted);
    }


    @Test
    public void shouldReturnFalseForSingleQuoteOnlyAtEnd() {
        boolean quoted = EntityNameUtils.isQuoted("a'");
        assertEquals(false, quoted);
    }

    @Test
    public void shouldReturnTrueForEmptyStringSurroundedBySingleQuotes() {
        boolean quoted = EntityNameUtils.isQuoted("''");
        assertEquals(true, quoted);
    }

    @Test
    public void shouldReturnTrueForSingleQuoteAtStartAndAtEnd() {
        boolean quoted = EntityNameUtils.isQuoted("'a'");
        assertEquals(true, quoted);
    }
}
