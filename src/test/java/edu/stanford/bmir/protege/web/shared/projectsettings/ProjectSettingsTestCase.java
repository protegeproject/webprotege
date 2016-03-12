package edu.stanford.bmir.protege.web.shared.projectsettings;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectSettingsTestCase {

    @Mock
    private ProjectId projectId;

    @Mock
    private ProjectType projectType;

    private String projectDescription = "DESCRIPTION";

    private String projectDisplayName = "DISPLAY_NAME";


    private ProjectSettings data;


    @Before
    public void setUp() throws Exception {
        data = new ProjectSettings(projectId, projectDisplayName, projectDescription);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfProjectIdIsNull() {
        new ProjectSettings(null, projectDisplayName, projectDescription);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfProjectDisplayNameIsNull() {
        new ProjectSettings(projectId, null, projectDescription);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfProjectDescriptionIsNull() {
        new ProjectSettings(projectId, projectDisplayName, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(data, is(equalTo(data)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(data, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        ProjectSettings other = new ProjectSettings(projectId, projectDisplayName, projectDescription);
        assertThat(data, is(equalTo(other)));
    }

    @Test
    public void shouldHaveSameHashCode() {
        ProjectSettings other = new ProjectSettings(projectId, projectDisplayName, projectDescription);
        assertThat(data.hashCode(), is(other.hashCode()));
    }


    @Test
    public void shouldReturnSuppliedProjectId() {
        assertThat(data.getProjectId(), is(projectId));
    }

    @Test
    public void shouldReturnSuppliedDisplayName() {
        assertThat(data.getProjectDisplayName(), is(projectDisplayName));
    }

    @Test
    public void shouldReturnSuppliedProjectDescription() {
        assertThat(data.getProjectDescription(), is(projectDescription));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(data.toString(), startsWith("ProjectSettings"));
        assertThat(data.toString(), containsString(projectDisplayName));
        assertThat(data.toString(), containsString(projectDescription));
    }
}
