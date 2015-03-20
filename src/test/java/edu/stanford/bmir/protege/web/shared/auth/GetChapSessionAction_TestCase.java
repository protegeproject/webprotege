package edu.stanford.bmir.protege.web.shared.auth;

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
 * 17/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetChapSessionAction_TestCase {


    private GetChapSessionAction action;

    private GetChapSessionAction otherAction;

    @Mock
    private UserId userId;


    @Before
    public void setUp() throws Exception {
        action = new GetChapSessionAction(userId);
        otherAction = new GetChapSessionAction(userId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new GetChapSessionAction(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(equalTo(action)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(action, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(equalTo(otherAction)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(action.hashCode(), is(otherAction.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(action.toString(), startsWith("GetChapSessionAction"));
    }

    @Test
    public void shouldReturnSuppliedUserId() {
        assertThat(action.getUserId(), is(userId));
    }
}