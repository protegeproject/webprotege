
package edu.stanford.bmir.protege.web.server.user;

import java.util.List;

import edu.stanford.bmir.protege.web.server.project.RecentProjectRecord;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserActivityRecord_TestCase {

    private UserActivityRecord userActivityRecord;

    @Mock
    private UserId userId;

    private long lastLogin = 1L;

    private long lastLogout = 2L;

    @Mock
    private RecentProjectRecord recentProject;

    private List<RecentProjectRecord> recentProjects;

    @Before
    public void setUp() {
        recentProjects = singletonList(recentProject);
        userActivityRecord = new UserActivityRecord(userId, lastLogin, lastLogout, recentProjects);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new UserActivityRecord(null, lastLogin, lastLogout, recentProjects);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(userActivityRecord.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_lastLogin() {
        assertThat(userActivityRecord.getLastLogin(), is(this.lastLogin));
    }

    @Test
    public void shouldReturnSupplied_lastLogout() {
        assertThat(userActivityRecord.getLastLogout(), is(this.lastLogout));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_recentProjects_IsNull() {
        new UserActivityRecord(userId, lastLogin, lastLogout, null);
    }

    @Test
    public void shouldReturnSupplied_recentProjects() {
        assertThat(userActivityRecord.getRecentProjects(), is(this.recentProjects));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(userActivityRecord, is(userActivityRecord));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(userActivityRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(userActivityRecord, is(new UserActivityRecord(userId, lastLogin, lastLogout, recentProjects)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(userActivityRecord, is(not(new UserActivityRecord(mock(UserId.class), lastLogin, lastLogout, recentProjects))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_lastLogin() {
        assertThat(userActivityRecord, is(not(new UserActivityRecord(userId, 3L, lastLogout, recentProjects))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_lastLogout() {
        assertThat(userActivityRecord, is(not(new UserActivityRecord(userId, lastLogin, 4L, recentProjects))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_recentProjects() {
        assertThat(userActivityRecord, is(not(new UserActivityRecord(userId, lastLogin, lastLogout,
                                                                     singletonList(mock(RecentProjectRecord.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(userActivityRecord.hashCode(), is(new UserActivityRecord(userId, lastLogin, lastLogout, recentProjects).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(userActivityRecord.toString(), Matchers.startsWith("UserActivityRecord"));
    }
}
