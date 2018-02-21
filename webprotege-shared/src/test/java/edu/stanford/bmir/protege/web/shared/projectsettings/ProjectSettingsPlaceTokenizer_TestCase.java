package edu.stanford.bmir.protege.web.shared.projectsettings;

import edu.stanford.bmir.protege.web.shared.place.ProjectSettingsPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectSettingsPlaceTokenizer_TestCase {

    private static final String PROJECT_ID = "12345678-1234-1234-1234-123456789abc";

    private ProjectSettingsPlaceTokenizer tokenizer;

    @Mock
    private ProjectSettingsPlace place;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() {
        when(projectId.getId()).thenReturn(PROJECT_ID);
        tokenizer = new ProjectSettingsPlaceTokenizer();
        when(place.getProjectId()).thenReturn(projectId);
    }

    @Test
    public void shouldTokenizePlace() {
        String token = tokenizer.getToken(place);
        assertThat(token, is("projects/"+PROJECT_ID+"/settings"));
    }

    @Test
    public void shouldGetPlaceFromToken() {
        ProjectSettingsPlace p = tokenizer.getPlace("projects/"+PROJECT_ID+"/settings");
        assertThat(p, is(not(nullValue())));
        assertThat(p.getProjectId().getId(), is(PROJECT_ID));
    }

    @Test
    public void shouldNotGetPlaceFromMalformedProjectId() {
        ProjectSettingsPlace p = tokenizer.getPlace("projects/MALFORMED/settings");
        assertThat(p, is(nullValue()));
    }
}
