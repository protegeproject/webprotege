package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@RunWith(MockitoJUnitRunner.class)
public class UserWatch_TestCase<W> {


    private UserWatch<W> userWatch;

    private UserWatch<W> otherUserWatch;

    @Mock
    private UserId userId;

    @Mock
    private Watch<W> watch;


    @Before
    public void setUp() throws Exception {
        userWatch = new UserWatch<>(userId, watch);
        otherUserWatch = new UserWatch<>(userId, watch);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_UserId_IsNull() {
        new UserWatch<>(null, watch);
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Watch_IsNull() {
        new UserWatch<>(userId, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(userWatch, is(equalTo(userWatch)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(userWatch, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(userWatch, is(equalTo(otherUserWatch)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(userWatch.hashCode(), is(otherUserWatch.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(userWatch.toString(), startsWith("UserWatch"));
    }

    @Test
    public void shouldReturnSupplied_UserId() {
        assertThat(userWatch.getUserId(), is(userId));
    }

    @Test
    public void shouldReturnSupplied_Watch() {
        assertThat(userWatch.getWatch(), is(watch));
    }
}