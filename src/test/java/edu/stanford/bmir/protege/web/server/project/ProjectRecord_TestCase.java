
package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProjectRecord_TestCase {

    private ProjectRecord projectRecord;

    @Mock

    private ProjectId projectId;

    @Mock
    private UserId projectOwner;

    private String displayName = "The displayName";

    private String projectDescription = "The projectDescription";

    private boolean inTrash = true;

    @Before
    public void setUp() {
        projectRecord = new ProjectRecord(projectId, projectOwner, displayName, projectDescription, inTrash);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectRecord(null, projectOwner, displayName, projectDescription, inTrash);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectRecord.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectOwner_IsNull() {
        new ProjectRecord(projectId, null, displayName, projectDescription, inTrash);
    }

    @Test
    public void shouldReturnSupplied_projectOwner() {
        assertThat(projectRecord.getOwner(), is(this.projectOwner));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_displayName_IsNull() {
        new ProjectRecord(projectId, projectOwner, null, projectDescription, inTrash);
    }

    @Test
    public void shouldReturnSupplied_displayName() {
        assertThat(projectRecord.getDisplayName(), is(this.displayName));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDescription_IsNull() {
        new ProjectRecord(projectId, projectOwner, displayName, null, inTrash);
    }

    @Test
    public void shouldReturnSupplied_projectDescription() {
        assertThat(projectRecord.getDescription(), is(this.projectDescription));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectRecord, is(projectRecord));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(projectRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectRecord, is(new ProjectRecord(projectId, projectOwner, displayName, projectDescription, inTrash)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectRecord, is(not(new ProjectRecord(mock(ProjectId.class), projectOwner, displayName, projectDescription, inTrash))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectOwner() {
        assertThat(projectRecord, is(not(new ProjectRecord(projectId, mock(UserId.class), displayName, projectDescription, inTrash))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_displayName() {
        assertThat(projectRecord, is(not(new ProjectRecord(projectId, projectOwner, "String-86849d1f-8c1e-448a-8e04-4b5b2f42e48d", projectDescription, inTrash))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectDescription() {
        assertThat(projectRecord, is(not(new ProjectRecord(projectId, projectOwner, displayName, "String-8de527ce-1b5b-4815-a849-9645838a075e", inTrash))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_inTrash() {
        assertThat(projectRecord, is(not(new ProjectRecord(projectId, projectOwner, displayName, projectDescription, false))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectRecord.hashCode(), is(new ProjectRecord(projectId, projectOwner, displayName, projectDescription, inTrash).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectRecord.toString(), startsWith("ProjectRecord"));
    }

    @Test
    public void shouldReturn_true_For_isInTrash() {
        assertThat(projectRecord.isInTrash(), is(true));
    }

}
