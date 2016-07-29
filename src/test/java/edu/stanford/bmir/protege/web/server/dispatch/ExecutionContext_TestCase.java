package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ExecutionContext_TestCase {

    @Mock
    private WebProtegeSession session;

    @Mock
    private UserId userId;

    private ExecutionContext context;

    @Before
    public void setUp() throws Exception {
        context = new ExecutionContext(session);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerForNullSession() {
        new ExecutionContext(null);
    }

    @Test
    public void shouldReturnSuppliedSession() {
        assertThat(context.getSession(), is(session));
    }

    @Test
    public void shouldReturnGuestUser() {
        when(session.getUserInSession()).thenReturn(UserId.getGuest());
        assertThat(context.getUserId(), is(UserId.getGuest()));
    }

    @Test
    public void shouldReturnUserId() {
        when(session.getUserInSession()).thenReturn(userId);
        assertThat(context.getUserId(), is(userId));
    }

}
