package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionAttribute;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.server.user.UserActivityManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeSessionListener_TestCase {

    private WebProtegeSessionListener listener;

    @Mock
    private UserActivityManager activityManager;

    @Mock
    private WebProtegeLogger logger;

    @Mock
    private HttpSessionEvent sessionEvent;

    @Mock
    private HttpSession session;

    @Mock
    private UserId userId;

    @Before
    public void setUp() throws Exception {
        listener = new WebProtegeSessionListener(activityManager, logger);
        when(sessionEvent.getSession()).thenReturn(session);
        when(session.getAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER.getAttributeName())).thenReturn(userId);
    }

    @Test
    public void shouldRecordUserLoggedOut() {
        listener.sessionDestroyed(sessionEvent);
        verify(activityManager, times(1)).setLastLogout(eq(userId), anyLong());
    }
}
