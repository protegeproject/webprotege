package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetails_TestCase {


    private UserDetails userDetails;

    private UserDetails otherUserDetails;

    @Mock
    private UserId userId;

    private String displayName = "Display name";

    private Optional<String> emailAddress = Optional.of("Email Address");

    @Before
    public void setUp() throws Exception {
        userDetails = new UserDetails(userId, displayName, emailAddress);
        otherUserDetails = new UserDetails(userId, displayName, emailAddress);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_UserId_IsNull() {
        new UserDetails(null, displayName, emailAddress);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_DisplayName_IsNull() {
        new UserDetails(userId, null, emailAddress);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_EmailAddress_IsNull() {
        new UserDetails(userId, displayName, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(userDetails, is(equalTo(userDetails)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(userDetails, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(userDetails, is(equalTo(otherUserDetails)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(userDetails.hashCode(), is(otherUserDetails.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(userDetails.toString(), startsWith("UserDetails"));
    }

    @Test
    public void shouldReturnSuppliedUserId() {
        assertThat(userDetails.getUserId(), is(userId));
    }

    @Test
    public void shouldReturnSuppliedDisplayName() {
        assertThat(userDetails.getDisplayName(), is(displayName));
    }

    @Test
    public void shouldReturnSuppliedEmailAddress() {
        assertThat(userDetails.getEmailAddress(), is(emailAddress));
    }

    @Test
    public void shouldReturnGuestDetails() {
        assertThat(UserDetails.getGuestUserDetails().getUserId(), is(UserId.getGuest()));
    }

    @Test
    public void shouldBeEqualToGuestUser() {
        assertThat(UserDetails.getGuestUserDetails(), is(new UserDetails(UserId.getGuest(), "Guest", Optional.<String>absent())));
    }

}