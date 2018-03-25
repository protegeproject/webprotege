package edu.stanford.bmir.protege.web.shared.place;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public class ProjectTagsPlaceTokenizer_TestCase {

    private static final String PROJECT_ID = "12345678-1234-1234-1234-123456789abc";

    private static final String PLACE_TOKEN = "projects/" + PROJECT_ID + "/tags";

    private ProjectTagsPlaceTokenizer tokenizer;

    @Before
    public void setUp() throws Exception {
        tokenizer = new ProjectTagsPlaceTokenizer();
    }

    @Test
    public void shouldParsePlaceToken() {
        ProjectTagsPlace place = tokenizer.getPlace(PLACE_TOKEN);
        assertThat(place, is(not(nullValue())));
        assertThat(place.getProjectId().getId(), is(PROJECT_ID));
    }

    @Test
    public void shouldGenerateToken() {
        ProjectTagsPlace place = new ProjectTagsPlace(ProjectId.get(PROJECT_ID), Optional.empty());
        String token = tokenizer.getToken(place);
        assertThat(token, is(PLACE_TOKEN));
    }
}
