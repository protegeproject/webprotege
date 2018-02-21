
package edu.stanford.bmir.protege.web.shared.user;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserIdProjectIdKey_TestCase {

    private UserIdProjectIdKey userIdProjectIdKey;
    @Mock
    private UserId userId;
    @Mock
    private ProjectId projectId;

    @Before
    public void setUp()
        throws Exception
    {
        userIdProjectIdKey = new UserIdProjectIdKey(userId, projectId);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new UserIdProjectIdKey(null, projectId);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(userIdProjectIdKey.getUserId(), is(this.userId));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new UserIdProjectIdKey(userId, null);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(userIdProjectIdKey.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(userIdProjectIdKey, is(userIdProjectIdKey));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(userIdProjectIdKey.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(userIdProjectIdKey, is(new UserIdProjectIdKey(userId, projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(userIdProjectIdKey, is(Matchers.not(new UserIdProjectIdKey(mock(UserId.class), projectId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(userIdProjectIdKey, is(Matchers.not(new UserIdProjectIdKey(userId, mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(userIdProjectIdKey.hashCode(), is(new UserIdProjectIdKey(userId, projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(userIdProjectIdKey.toString(), Matchers.startsWith("UserIdProjectIdKey"));
    }

}
