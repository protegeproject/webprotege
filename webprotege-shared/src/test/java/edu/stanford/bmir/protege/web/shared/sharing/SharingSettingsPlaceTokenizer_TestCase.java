package edu.stanford.bmir.protege.web.shared.sharing;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SharingSettingsPlaceTokenizer_TestCase {


    public static final String PROJECT_ID = "12345678-1234-1234-1234-123456789abc";

    private SharingSettingsPlaceTokenizer tokenizer;

    @Mock
    private ProjectId projectId;

    @Mock
    private SharingSettingsPlace place;

    private final String EXPECTED_TOKEN = "projects/" + PROJECT_ID + "/sharing";

    @Before
    public void setUp() {
        when(projectId.getId()).thenReturn(PROJECT_ID);
        when(place.getProjectId()).thenReturn(projectId);
        tokenizer = new SharingSettingsPlaceTokenizer();

    }

    @Test
    public void shouldTokenizePlace() {
        String token = tokenizer.getToken(place);
        assertThat(token, is(EXPECTED_TOKEN));
    }

    @Test
    public void shouldParseToken() {
        SharingSettingsPlace place = tokenizer.getPlace(EXPECTED_TOKEN);
        assertThat(place.getProjectId().getId(), is(PROJECT_ID));
    }
}
