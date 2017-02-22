package edu.stanford.bmir.protege.web.server.access;

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
public class Subject_SpecificUser_TestCase {

    public static final String THE_USER = "TheUser";

    private Subject subject;

    @Before
    public void setUp() {
        subject = Subject.forUser(THE_USER);
    }

    @Test
    public void shouldReturnFalseForIsGuest() {
        assertThat(subject.isGuest(), is(false));
    }

    @Test
    public void shouldReturnUserName() {
        assertThat(subject.getUserName(), is(Optional.of(THE_USER)));
    }

    @Test
    public void shouldReturnFalseForIsAnySignedInUser() {
        assertThat(subject.isAnySignedInUser(), is(false));
    }

    @Test
    public void shouldEqualOtherUser() {
        assertThat(subject, is(Subject.forUser(THE_USER)));
    }

    @Test
    public void shouldNotBeEqualToAnySignedInUser() {
        Subject anySignedInUser = Subject.forAnySignedInUser();
        assertThat(this.subject, is(not(anySignedInUser)));
    }

    @Test
    public void shouldNotBeEqualToGuestUser() {
        Subject otherUser = Subject.forGuestUser();
        assertThat(this.subject, is(not(otherUser)));
    }
}
