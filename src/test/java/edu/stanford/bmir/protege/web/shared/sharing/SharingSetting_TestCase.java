
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
public class SharingSetting_TestCase {

    private SharingSetting sharingSetting;
    @Mock
    private UserId userId;

    private SharingPermission sharingPermission = SharingPermission.EDIT;

    @Before
    public void setUp()
        throws Exception
    {
        sharingSetting = new SharingSetting(userId, sharingPermission);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new SharingSetting(null, sharingPermission);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        MatcherAssert.assertThat(sharingSetting.getUserId(), Matchers.is(this.userId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_sharingSetting_IsNull() {
        new SharingSetting(userId, null);
    }

    @Test
    public void shouldReturnSupplied_sharingSetting() {
        MatcherAssert.assertThat(sharingSetting.getSharingPermission(), Matchers.is(this.sharingPermission));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(sharingSetting, Matchers.is(sharingSetting));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(sharingSetting.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(sharingSetting, Matchers.is(new SharingSetting(userId, sharingPermission)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        MatcherAssert.assertThat(sharingSetting, Matchers.is(Matchers.not(new SharingSetting(Mockito.mock(UserId.class), sharingPermission))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_sharingSetting() {
        MatcherAssert.assertThat(sharingSetting, Matchers.is(Matchers.not(new SharingSetting(userId, SharingPermission.COMMENT))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(sharingSetting.hashCode(), Matchers.is(new SharingSetting(userId, sharingPermission).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(sharingSetting.toString(), Matchers.startsWith("UserSharingSetting"));
    }
}
