package edu.stanford.bmir.protege.web.shared.place;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Feb 2018
 */
public class ProjectPrefixDeclarationsPlaceTokenizer_TestCase {

    private static final String PROJECT_ID = "12345678-1234-1234-1234-123456789abc";

    private static final String PLACE_URL = "projects/" + PROJECT_ID + "/prefixes";

    private ProjectPrefixDeclarationsPlaceTokenizer tokenizer;

    @Before
    public void setUp() throws Exception {
        tokenizer = new ProjectPrefixDeclarationsPlaceTokenizer();
    }

    @Test
    public void shouldMatchPlaceUrl() {
        boolean matches = tokenizer.matches(PLACE_URL);
        assertThat(matches, is(true));
    }

    @Test
    public void shouldParsePlaceUrl() {
        ProjectPrefixDeclarationsPlace place = tokenizer.getPlace(PLACE_URL);
        assertThat(place, is(notNullValue()));
        assertThat(place.getProjectId().getId(), is(PROJECT_ID));
    }

    @Test
    public void shouldNotParsePlaceUrl() {
        ProjectPrefixDeclarationsPlace place = tokenizer.getPlace("other/url");
        assertThat(place, is(nullValue()));
    }
}
