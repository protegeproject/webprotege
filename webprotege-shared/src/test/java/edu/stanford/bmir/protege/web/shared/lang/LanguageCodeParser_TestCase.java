package edu.stanford.bmir.protege.web.shared.lang;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class LanguageCodeParser_TestCase {

    private LanguageCodeParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new LanguageCodeParser();
    }

    @Test
    public void shouldParseInput() {
        String input = "\"en\",\"English\"\n" +
                "\"fr\",\"French\"\n";
        Collection<LanguageCode> codes = parser.parse(input);
        assertThat(codes, hasItem(new LanguageCode("en", "English")));
        assertThat(codes, hasItem(new LanguageCode("fr", "French")));
    }

    @Test
    public void shouldSkipMalformedLines() {
        String input = "\"e\",\"English\"\n" +
                "\"fr\",\"French\"\n";
        Collection<LanguageCode> codes = parser.parse(input);
        assertThat(codes, not(hasItem(new LanguageCode("en", "English"))));
        assertThat(codes, hasItem(new LanguageCode("fr", "French")));
    }
}
