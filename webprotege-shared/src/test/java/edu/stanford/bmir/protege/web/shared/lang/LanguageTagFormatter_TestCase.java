package edu.stanford.bmir.protege.web.shared.lang;

import org.junit.Test;

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class LanguageTagFormatter_TestCase {


    @Test
    public void shouldFormatLanguageTagAsLowerCase() {
        formatAndTest("EN", "en");
    }

    @Test
    public void shouldFormatRegionAsUpperCase() {
        formatAndTest("en-gb", "en-GB");
    }

    @Test
    public void shouldHandleMalformedLanguageTag() {
        formatAndTest("en-", "en-");
    }

    @Test
    public void shouldHandleMalformedLanguageTag2() {
        formatAndTest("-en", "-EN");
    }

    @Test
    public void shouldHandleMalformedLanguageTag3() {
        formatAndTest("--en-", "--EN-");
    }


    private static void formatAndTest(@Nonnull String input, @Nonnull String expected) {
        String formatted = LanguageTagFormatter.format(input);
        assertThat(formatted, is(expected));
    }
}
