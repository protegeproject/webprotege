package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;


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
    private DispatchServiceCallback<AuthenticationResponse> callback;

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


    @Before
    public void setUp() throws Exception {
        protocol = new AuthenticatedActionExecutor(dispatchServiceManager, passwordDigestAlgorithm, chapResponseDigestAlgorithm);

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

        doAnswer(new Answer() {
            @Override
            @SuppressWarnings("unchecked")
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
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
            }
        }).when(dispatchServiceManager).execute(any(Action.class), any(DispatchServiceCallback.class));

        when(actionFactory.createAction(chapSessionId, userId, chapResponse)).thenReturn(action);
    }

    @Test
    public void shouldExecute_GetChapSession_ForUserId() {
        protocol.execute(userId, clearTextPassword, actionFactory, callback);
        verify(dispatchServiceManager, atLeastOnce()).execute(argThat(isAGetChapSessionAction()), anyGetChapSessionResultCallback());
    }

    @Test
    public void shouldExecute_AuthenticationAction_ForChapResponse() {
        protocol.execute(userId, clearTextPassword, actionFactory, callback);
        verify(dispatchServiceManager).execute(eq(action), Matchers.<DispatchServiceCallback<R>>any());
    }

    private Matcher<GetChapSessionAction> isAGetChapSessionAction() {
        return new TypeSafeMatcher<GetChapSessionAction>() {
            @Override
            protected boolean matchesSafely(GetChapSessionAction item) {
                return item.getUserId().equals(userId);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("GetChapSessionAction with userId " + userId);
            }
        };
    }


    private DispatchServiceCallback<GetChapSessionResult> anyGetChapSessionResultCallback() {
        return Matchers.any();
    }
}
