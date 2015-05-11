
package edu.stanford.bmir.protege.web.shared.sharing;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.NullPointerException;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class UserSharingSetting_TestCase {

    private UserSharingSetting userSharingSetting;
    @Mock
    private UserId userId;

    private SharingPermission sharingPermission = SharingPermission.EDIT;

    @Before
    public void setUp()
        throws Exception
    {
        userSharingSetting = new UserSharingSetting(userId, sharingPermission);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new UserSharingSetting(null, sharingPermission);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        MatcherAssert.assertThat(userSharingSetting.getUserId(), Matchers.is(this.userId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_sharingSetting_IsNull() {
        new UserSharingSetting(userId, null);
    }

    @Test
    public void shouldReturnSupplied_sharingSetting() {
        MatcherAssert.assertThat(userSharingSetting.getSharingPermission(), Matchers.is(this.sharingPermission));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(userSharingSetting, Matchers.is(userSharingSetting));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(userSharingSetting.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(userSharingSetting, Matchers.is(new UserSharingSetting(userId, sharingPermission)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        MatcherAssert.assertThat(userSharingSetting, Matchers.is(Matchers.not(new UserSharingSetting(Mockito.mock(UserId.class), sharingPermission))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_sharingSetting() {
        MatcherAssert.assertThat(userSharingSetting, Matchers.is(Matchers.not(new UserSharingSetting(userId, SharingPermission.COMMENT))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(userSharingSetting.hashCode(), Matchers.is(new UserSharingSetting(userId, sharingPermission).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(userSharingSetting.toString(), Matchers.startsWith("UserSharingSetting"));
    }
}
