
package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.NullPointerException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlListEntry_TestCase {

    private AccessControlListEntry accessControlListEntry;
    @Mock
    private ProjectId projectId;
    @Mock
    private UserId userId;
    @Mock
    private Permission permission;

    @Before
    public void setUp()
    {
        accessControlListEntry = new AccessControlListEntry(projectId, userId, permission);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new AccessControlListEntry(null, userId, permission);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(accessControlListEntry.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new AccessControlListEntry(projectId, null, permission);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(accessControlListEntry.getUserId(), is(this.userId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_permission_IsNull() {
        new AccessControlListEntry(projectId, userId, null);
    }

    @Test
    public void shouldReturnSupplied_permission() {
        assertThat(accessControlListEntry.getPermission(), is(this.permission));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(accessControlListEntry, is(accessControlListEntry));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(accessControlListEntry.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(accessControlListEntry, is(new AccessControlListEntry(projectId, userId, permission)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(accessControlListEntry, is(not(new AccessControlListEntry(mock(ProjectId.class), userId, permission))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(accessControlListEntry, is(not(new AccessControlListEntry(projectId, mock(UserId.class), permission))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_permission() {
        assertThat(accessControlListEntry, is(not(new AccessControlListEntry(projectId, userId, mock(Permission.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(accessControlListEntry.hashCode(), is(new AccessControlListEntry(projectId, userId, permission).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(accessControlListEntry.toString(), startsWith("AccessControlListEntry"));
    }

}
