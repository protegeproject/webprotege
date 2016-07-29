
package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
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
public class ProjectPermissionRecord_TestCase {

    private ProjectPermissionRecord projectPermissionRecord;
    @Mock
    private ProjectId projectId;
    @Mock
    private UserId userId;
    @Mock
    private ImmutableSet<Permission> permissions;

    @Before
    public void setUp()
    {
        projectPermissionRecord = new ProjectPermissionRecord(projectId, userId, permissions);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectPermissionRecord(null, userId, permissions);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectPermissionRecord.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new ProjectPermissionRecord(projectId, null, permissions);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(projectPermissionRecord.getUserId(), is(this.userId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_permission_IsNull() {
        new ProjectPermissionRecord(projectId, userId, null);
    }

    @Test
    public void shouldReturnSupplied_permission() {
        assertThat(projectPermissionRecord.getPermissions(), is(this.permissions));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectPermissionRecord, is(projectPermissionRecord));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(projectPermissionRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectPermissionRecord, is(new ProjectPermissionRecord(projectId, userId, permissions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectPermissionRecord, is(not(new ProjectPermissionRecord(mock(ProjectId.class), userId, permissions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(projectPermissionRecord, is(not(new ProjectPermissionRecord(projectId, mock(UserId.class), permissions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_permission() {
        assertThat(projectPermissionRecord, is(not(new ProjectPermissionRecord(projectId, userId, mock(ImmutableSet.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectPermissionRecord.hashCode(), is(new ProjectPermissionRecord(projectId, userId, permissions).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectPermissionRecord.toString(), startsWith("ProjectPermissionRecord"));
    }

}
