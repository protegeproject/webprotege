package edu.stanford.bmir.protege.web.server.auth;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class PerformLoginActionHandler_TestCase {

    private PerformLoginActionHandler handler;

    @Mock
    private ChapSessionManager sessionManager;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ChapResponseChecker responseChecker;

    @Mock
    private WebProtegeLogger logger;

    @Mock
    private PerformLoginAction action;

    @Mock
    private UserId userId;

    @Mock
    private ChapSessionId chapSessionId;

    @Mock
    private ChapResponse chapResponse;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private Salt salt;

    @Mock
    private ChapSession chapSession;

    @Mock
    private SaltedPasswordDigest passwordDigest;

    @Mock
    private WebProtegeSession webProtegeSession;

    @Before
    public void setUp() throws Exception {
        handler = new PerformLoginActionHandler(sessionManager, authenticationManager, responseChecker, logger);
        when(action.getUserId()).thenReturn(userId);
        when(action.getChapSessionId()).thenReturn(chapSessionId);
        when(action.getChapResponse()).thenReturn(chapResponse);
        when(sessionManager.retrieveChallengeMessage(chapSessionId)).thenReturn(Optional.of(chapSession));

        when(authenticationManager.getSaltedPasswordDigest(userId)).thenReturn(Optional.of(passwordDigest));

        when(executionContext.getSession()).thenReturn(webProtegeSession);
    }

    @Test
    public void shouldFailOnTimeOut() {
        when(sessionManager.retrieveChallengeMessage(chapSessionId)).thenReturn(Optional.<ChapSession>absent());
        PerformLoginResult result = handler.execute(action, executionContext);
        assertThat(result.getResponse(), is(AuthenticationResponse.FAIL));
    }

    @Test
    public void shouldFailAuthentication() {
        when(responseChecker.isExpectedResponse(
                Mockito.any(ChapResponse.class),
                Mockito.any(ChallengeMessage.class),
                Mockito.any(SaltedPasswordDigest.class))).thenReturn(false);
        PerformLoginResult result = handler.execute(action, executionContext);
        assertThat(result.getResponse(), is(AuthenticationResponse.FAIL));

        verify(webProtegeSession, never()).setUserInSession(Mockito.any(UserId.class));
    }

    @Test
    public void shouldSetUserInSession() {
        when(responseChecker.isExpectedResponse(
                Mockito.any(ChapResponse.class),
                Mockito.any(ChallengeMessage.class),
                Mockito.any(SaltedPasswordDigest.class))).thenReturn(true);
        PerformLoginResult result = handler.execute(action, executionContext);
        assertThat(result.getResponse(), is(AuthenticationResponse.SUCCESS));

        verify(webProtegeSession, times(1)).setUserInSession(Mockito.any(UserId.class));
    }
}
