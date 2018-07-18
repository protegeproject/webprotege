package edu.stanford.bmir.protege.web.shared.place;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class LanguageSettingsPlaceTokenizer_TestCase {

    private LanguageSettingsPlaceTokenizer tokenizer;

    @Before
    public void setUp() {
        tokenizer = new LanguageSettingsPlaceTokenizer();
    }

    @Test
    public void shouldParsePlaceToken() {
        String projectId = "12345678-1234-1234-1234-123456789abc";
        LanguageSettingsPlace place = tokenizer.getPlace("projects/" + projectId + "/languages");
        assertThat(place, is(notNullValue()));
        assertThat(place.getProjectId().getId(), is(projectId));
    }

    @Test
    public void shouldNotParseMalformedProjectId() {
        LanguageSettingsPlace place = tokenizer.getPlace("projects/malformed/language");
        assertThat(place, is(nullValue()));
    }

}
