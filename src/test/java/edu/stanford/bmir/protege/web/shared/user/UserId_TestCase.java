package edu.stanford.bmir.protege.web.shared.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class UserId_TestCase {


    public static final String THE_USER_NAME = "The User Name";
    private UserId userId;

    private UserId otherUserId;


    @Before
    public void setUp() throws Exception {
        userId = UserId.getUserId(THE_USER_NAME);
        otherUserId = UserId.getUserId(THE_USER_NAME);
    }

    @Test
    public void shouldReturnGuestUser() {
        assertThat(UserId.getUserId(null), is(UserId.getGuest()));
    }

    @Test
    public void shouldReturnTrue() {
        assertThat(UserId.getGuest().isGuest(), is(true));
    }

    @Test
    public void shouldReturnFalse() {
        assertThat(userId.isGuest(), is(false));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(userId, is(equalTo(userId)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(userId, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(userId, is(equalTo(otherUserId)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(userId.hashCode(), is(otherUserId.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(userId.toString(), startsWith("UserId"));
    }

    @Test
    public void shouldReturnSuppliedUserName() {
        assertThat(userId.getUserName(), is(THE_USER_NAME));
    }

    @Test
    public void shouldCompareByUserName() {
        UserId userIdA = UserId.getUserId("a");
        UserId userIdB = UserId.getUserId("b");
        assertThat(userIdA.compareTo(userIdB), is(lessThan(0)));
    }

    @Test
    public void shouldCompareIgnoringCase() {
        // 'a' should come before 'B' even though the ASCII code for 'B' is smaller than 'a'
        UserId userIdA = UserId.getUserId("a");
        UserId userIdB = UserId.getUserId("B");
        assertThat(userIdA.compareTo(userIdB), is(lessThan(0)));
    }

    @Test
    public void shouldCompareWithCaseIfCompareIgnoreCaseIsEqual() {
        // 'a' should come after 'A'
        UserId userIdA = UserId.getUserId("a");
        UserId userIdB = UserId.getUserId("A");
        assertThat(userIdA.compareTo(userIdB), is(greaterThan(0)));
    }
}