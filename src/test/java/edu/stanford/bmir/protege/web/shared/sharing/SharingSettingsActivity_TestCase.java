
package edu.stanford.bmir.protege.web.shared.sharing;

import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsPresenter;
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

@RunWith(MockitoJUnitRunner.class)
public class SharingSettingsActivity_TestCase {

    private SharingSettingsActivity sharingSettingsActivity;
    @Mock
    private SharingSettingsPresenter presenter;
    @Mock
    private SharingSettingsPlace place;

    @Before
    public void setUp()
    {
        sharingSettingsActivity = new SharingSettingsActivity(presenter, place);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_presenter_IsNull() {
        new SharingSettingsActivity(null, place);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_place_IsNull() {
        new SharingSettingsActivity(presenter, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(sharingSettingsActivity, is(sharingSettingsActivity));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(sharingSettingsActivity.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(sharingSettingsActivity, is(new SharingSettingsActivity(presenter, place)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_presenter() {
        assertThat(sharingSettingsActivity, is(not(new SharingSettingsActivity(Mockito.mock(SharingSettingsPresenter.class), place))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_place() {
        assertThat(sharingSettingsActivity, is(not(new SharingSettingsActivity(presenter, Mockito.mock(SharingSettingsPlace.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(sharingSettingsActivity.hashCode(), is(new SharingSettingsActivity(presenter, place).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(sharingSettingsActivity.toString(), startsWith("SharingSettingsActivity"));
    }
}
