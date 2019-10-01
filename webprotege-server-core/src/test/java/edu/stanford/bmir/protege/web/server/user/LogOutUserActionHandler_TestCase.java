package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class LogOutUserActionHandler_TestCase {

    @Mock
    private WebProtegeSession session;

    @Mock
    private LogOutUserAction action;

    @Mock
    private ExecutionContext executionContext;

    private LogOutUserActionHandler actionHandler;

    @Mock
    private UserActivityManager activityManager;

    private UserId userId = UserId.getUserId("OtherUserId");

    @Mock
    private UserInSessionFactory userInSessionFactory;

    @Mock
    private UserInSession userInSession;

    @Mock
    private UserInSession guestUserInSession;

    @Mock
    private UserDetails userDetails;


    @Before
    public void setUp() throws Exception {
        when(userInSessionFactory.getUserInSession(UserId.getGuest())).thenReturn(guestUserInSession);
        when(executionContext.getSession()).thenReturn(session);
        when(session.getUserInSession()).thenReturn(userId);
        actionHandler = new LogOutUserActionHandler(activityManager, userInSessionFactory);
    }

    @Test
    public void shouldClearUserFromSession() {
        actionHandler.execute(action, executionContext);
        verify(session, times(1)).clearUserInSession();
    }

    @Test
    public void shouldSetLoggedOutTimestamp() {
        actionHandler.execute(action, executionContext);
        verify(activityManager, times(1)).setLastLogout(any(), anyLong());
    }
}
