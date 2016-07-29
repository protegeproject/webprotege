package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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



    @Before
    public void setUp() throws Exception {
        actionHandler = new LogOutUserActionHandler();
        when(executionContext.getSession()).thenReturn(session);
    }

    @Test
    public void shouldClearUserFromSession() {
        actionHandler.execute(action, executionContext);
        verify(session, times(1)).clearUserInSession();
    }
}
