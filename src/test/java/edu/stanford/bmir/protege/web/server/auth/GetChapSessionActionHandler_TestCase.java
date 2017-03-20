package edu.stanford.bmir.protege.web.server.auth;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetChapSessionActionHandler_TestCase {

    private GetChapSessionActionHandler handler;

    @Mock
    private WebProtegeLogger logger;

    @Mock
    private ChapSessionManager sessionManager;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Salt salt;

    @Mock
    private SaltedPasswordDigest saltedPasswordDigest;

    @Mock
    private UserId userId;

    @Mock
    private ChapSession chapSession;

    @Mock
    private ChapResponse chapResponse;

    @Mock
    private ChapSessionId chapSessionId;

    @Mock
    private GetChapSessionAction action;


    @Before
    public void setUp() throws Exception {
        handler = new GetChapSessionActionHandler(sessionManager, authenticationManager, logger);
        when(authenticationManager.getSalt(userId)).thenReturn(Optional.of(salt));
        when(authenticationManager.getSaltedPasswordDigest(userId)).thenReturn(Optional.of(saltedPasswordDigest));
        when(sessionManager.getSession(salt)).thenReturn(chapSession);

        when(chapSession.getId()).thenReturn(chapSessionId);
        when(chapSession.getSalt()).thenReturn(salt);

        when(action.getUserId()).thenReturn(userId);

    }

    @Test
    public void shouldReturnAbsentIfUserIsGuest() {
        when(userId.isGuest()).thenReturn(true);
        GetChapSessionResult result = handler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getChapSession(), is(Optional.<ChapSession>absent()));
    }

    @Test
    public void shouldReturnAbsentIfSaltIsAbsent() {
        when(authenticationManager.getSalt(userId)).thenReturn(Optional.absent());
        GetChapSessionResult result = handler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getChapSession(), is(Optional.<ChapSession>absent()));
    }

    @Test
    public void shouldGetChapSessionForKnownUserWithSalt() {
        GetChapSessionResult result = handler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getChapSession().isPresent(), is(true));
        ChapSession session = result.getChapSession().get();
        assertThat(session.getSalt(), is(salt));
    }
}
