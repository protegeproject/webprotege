
package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.NullPointerException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class WorldProjectPermissionRecord_TestCase {

    private WorldProjectPermissionRecord worldProjectPermissionRecord;

    @Mock
    private ProjectId projectId;

    @Mock
    private ImmutableSet<Permission> permissions;

    @Before
    public void setUp()
    {
        worldProjectPermissionRecord = new WorldProjectPermissionRecord(projectId, permissions);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new WorldProjectPermissionRecord(null, permissions);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(worldProjectPermissionRecord.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_permission_IsNull() {
        new WorldProjectPermissionRecord(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_permission() {
        assertThat(worldProjectPermissionRecord.getPermissions(), is(this.permissions));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(worldProjectPermissionRecord, is(worldProjectPermissionRecord));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(worldProjectPermissionRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(worldProjectPermissionRecord, is(new WorldProjectPermissionRecord(projectId, permissions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(worldProjectPermissionRecord, is(not(new WorldProjectPermissionRecord(mock(ProjectId.class), permissions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_permission() {
        assertThat(worldProjectPermissionRecord, is(not(new WorldProjectPermissionRecord(projectId, mock(ImmutableSet.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(worldProjectPermissionRecord.hashCode(), is(new WorldProjectPermissionRecord(projectId, permissions).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(worldProjectPermissionRecord.toString(), Matchers.startsWith("WorldProjectPermissionRecord"));
    }

}
