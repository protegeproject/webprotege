package edu.stanford.bmir.protege.web.client.auth;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticatedActionExecutor_TestCase<A extends AbstractAuthenticationAction<R>, R extends AbstractAuthenticationResult> {

    @Mock
    private DispatchServiceManager dispatchServiceManager;

    @Mock
    private PasswordDigestAlgorithm passwordDigestAlgorithm;

    @Mock
    private ChapResponseDigestAlgorithm chapResponseDigestAlgorithm;


    private AuthenticatedActionExecutor protocol;

    @Mock
    private UserId userId;

    @Mock
    private AuthenticatedDispatchServiceCallback<R> callback;

    private String clearTextPassword = "ThePassword";

    @Mock
    private GetChapSessionResult chapSessionResult;

    @Mock
    private ChapResponse chapResponse;

    @Mock
    private SaltedPasswordDigest saltedPassword;

    @Mock
    private ChapSession chapSession;

    @Mock
    private ChapSessionId chapSessionId;

    @Mock
    private Salt salt;

    @Mock
    private ChallengeMessage challengeMessage;

    @Mock
    private AuthenticationActionFactory<A, R> actionFactory;

    @Mock
    private A action;

    @Mock
    private R result;

    @Mock
    private DispatchErrorMessageDisplay errorDisplay;


    @Before
    public void setUp() throws Exception {
        protocol = new AuthenticatedActionExecutor(dispatchServiceManager, passwordDigestAlgorithm, chapResponseDigestAlgorithm, errorDisplay);

        when(passwordDigestAlgorithm
                .getDigestOfSaltedPassword(anyString(), any(Salt.class)))
                .thenReturn(saltedPassword);

        when(chapResponseDigestAlgorithm
                .getChapResponseDigest(any(ChallengeMessage.class), any(SaltedPasswordDigest.class)))
                .thenReturn(chapResponse);

        when(chapSessionResult.getChapSession()).thenReturn(Optional.of(chapSession));
        when(chapSession.getId()).thenReturn(chapSessionId);
        when(chapSession.getSalt()).thenReturn(salt);
        when(chapSession.getChallengeMessage()).thenReturn(challengeMessage);

        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            if (args[0] instanceof GetChapSessionAction) {
                DispatchServiceCallback<GetChapSessionResult> cb = (DispatchServiceCallback<GetChapSessionResult>) args[1];
                cb.onSuccess(chapSessionResult);
            }
            else if(args[1] instanceof PerformLoginAction) {
                DispatchServiceCallback<R> cb = (DispatchServiceCallback<R>) args[1];
                cb.onSuccess(result);
            }
            return null;
        }).when(dispatchServiceManager).execute(any(Action.class), any(DispatchServiceCallback.class));

        when(actionFactory.createAction(chapSessionId, userId, chapResponse)).thenReturn(action);
    }

    @Test
    public void shouldExecute_GetChapSession_ForUserId() {
        protocol.execute(userId, clearTextPassword, actionFactory, callback);
        verify(dispatchServiceManager, atLeastOnce())
                .execute(argThat(isAGetChapSessionAction()), anyGetChapSessionResultCallback());
    }

    @Test
    public void shouldExecute_AuthenticationAction_ForChapResponse() {
        protocol.execute(userId, clearTextPassword, actionFactory, callback);
        verify(dispatchServiceManager).execute(eq(action), Matchers.<DispatchServiceCallback<R>>any());
    }

    private ArgumentMatcher<GetChapSessionAction> isAGetChapSessionAction() {
        return argument -> argument.getUserId().equals(userId);
    }


    private DispatchServiceCallback<GetChapSessionResult> anyGetChapSessionResultCallback() {
        return Matchers.any();
    }
}
