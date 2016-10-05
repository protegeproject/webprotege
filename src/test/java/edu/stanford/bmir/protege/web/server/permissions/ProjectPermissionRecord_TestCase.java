
package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.collect.ImmutableSet;
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

import java.util.Optional;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ProjectPermissionRecord_TestCase {

    private ProjectPermissionRecord projectPermissionRecord;

    @Mock
    private ProjectId projectId;

    private Optional<UserId> userId = Optional.of(UserId.getUserId("The User"));

    @Mock
    private ImmutableSet<Permission> permissions;

    @Before
    public void setUp()
            throws Exception {
        projectPermissionRecord = new ProjectPermissionRecord(projectId, userId, permissions);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new ProjectPermissionRecord(null, userId, permissions);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(projectPermissionRecord.getProjectId(), Matchers.is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new ProjectPermissionRecord(projectId, null, permissions);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        MatcherAssert.assertThat(projectPermissionRecord.getUserId(), Matchers.is(this.userId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_permissions_IsNull() {
        new ProjectPermissionRecord(projectId, userId, null);
    }

    @Test
    public void shouldReturnSupplied_permissions() {
        MatcherAssert.assertThat(projectPermissionRecord.getPermissions(), Matchers.is(this.permissions));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(projectPermissionRecord, Matchers.is(projectPermissionRecord));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(projectPermissionRecord.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(projectPermissionRecord,
                                 Matchers.is(new ProjectPermissionRecord(projectId, userId, permissions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(projectPermissionRecord,
                                 Matchers.is(Matchers.not(new ProjectPermissionRecord(Mockito.mock(ProjectId.class),
                                                                                      userId,
                                                                                      permissions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        MatcherAssert.assertThat(projectPermissionRecord,
                                 Matchers.is(Matchers.not(new ProjectPermissionRecord(projectId,
                                                                                      Optional.of(UserId.getUserId("Other User")),
                                                                                      permissions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_permissions() {
        MatcherAssert.assertThat(projectPermissionRecord,
                                 Matchers.is(Matchers.not(new ProjectPermissionRecord(projectId,
                                                                                      userId,
                                                                                      ImmutableSet.of()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(projectPermissionRecord.hashCode(),
                                 Matchers.is(new ProjectPermissionRecord(projectId, userId, permissions).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(projectPermissionRecord.toString(), Matchers.startsWith("ProjectPermissionRecord"));
    }

}
