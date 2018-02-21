package edu.stanford.bmir.protege.web.server.access;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Feb 2017
 */
public class Subject_GuestUser_TestCase {

    private Subject subject;

    @Before
    public void setUp() {
        subject = Subject.forGuestUser();
    }

    @Test
    public void shouldReturnTrueForIsGuest() {
        assertThat(subject.isGuest(), is(true));
    }

    @Test
    public void shouldReturnGuestUserName() {
        assertThat(subject.getUserName(), is(Optional.of(UserId.getGuest().getUserName())));
    }

    @Test
    public void shouldReturnFalseForIsAnySignedInUser() {
        assertThat(subject.isAnySignedInUser(), is(false));
    }

    @Test
    public void shouldEqualOtherGuestSubject() {
        assertThat(subject, is(Subject.forGuestUser()));
    }

    @Test
    public void shouldNotBeEqualToAnySignedInUser() {
        Subject anySignedInUser = Subject.forAnySignedInUser();
        assertThat(this.subject, is(not(anySignedInUser)));
    }

    @Test
    public void shouldNotBeEqualToSpecificUser() {
        Subject otherUser = Subject.forUser("Other User");
        assertThat(this.subject, is(not(otherUser)));
    }
}
