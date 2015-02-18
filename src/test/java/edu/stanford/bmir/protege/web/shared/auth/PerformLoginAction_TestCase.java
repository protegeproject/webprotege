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
 * 18/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class PerformLoginAction_TestCase {


    private PerformLoginAction action;

    private PerformLoginAction otherAction;

    @Mock
    private UserId userId;

    @Mock
    private ChapSessionId chapSessionId;

    @Mock
    private ChapResponse chapResponse;

    @Before
    public void setUp() throws Exception {
        action = new PerformLoginAction(userId, chapSessionId, chapResponse);
        otherAction = new PerformLoginAction(userId, chapSessionId, chapResponse);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_UserId_IsNull() {
        new PerformLoginAction(null, chapSessionId, chapResponse);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ChapSessionId_IsNull() {
        new PerformLoginAction(userId, null, chapResponse);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ChapResponse_IsNull() {
        new PerformLoginAction(userId, chapSessionId, null);
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
        assertThat(action.toString(), startsWith("PerformLoginAction"));
    }

    @Test
    public void shouldReturnSuppliedChapSessionId() {
        assertThat(action.getChapSessionId(), is(chapSessionId));
    }

    @Test
    public void shouldReturnSuppliedUserId() {
        assertThat(action.getUserId(), is(userId));
    }

    @Test
    public void shouldReturnSuppliedChapResponse() {
        assertThat(action.getChapResponse(), is(chapResponse));
    }
}