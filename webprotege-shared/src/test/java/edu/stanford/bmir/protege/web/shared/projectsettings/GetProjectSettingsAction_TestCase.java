package edu.stanford.bmir.protege.web.shared.projectsettings;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectSettingsAction_TestCase {

    @Mock
    private ProjectId projectId;

    private GetProjectSettingsAction action;

    @Before
    public void setUp() throws Exception {
        action = new GetProjectSettingsAction(projectId);
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectSettings_IsNull() {
        new GetProjectSettingsResult(null);
    }

    @Test
    public void shouldReturnSupplied_ProjectId() {
        assertThat(action.getProjectId(), is(projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(equalTo(action)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(action, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        GetProjectSettingsAction other = new GetProjectSettingsAction(projectId);
        assertThat(action, is(equalTo(other)));
    }

    @Test
    public void shouldHaveSameHashCode() {
        GetProjectSettingsAction other = new GetProjectSettingsAction(projectId);
        assertThat(action.hashCode(), is(other.hashCode()));
    }
}
