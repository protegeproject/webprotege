package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 *
 * Executes an action that requires authentication.
 */
public class AuthenticatedActionExecutor {

    private final DispatchServiceManager dispatchServiceManager;

    private final PasswordDigestAlgorithm passwordDigestAlgorithm;

    private final ChapResponseDigestAlgorithm responseDigestAlgorithm;

    @Inject
    public AuthenticatedActionExecutor(DispatchServiceManager dispatchServiceManager,
                                       PasswordDigestAlgorithm passwordDigestAlgorithm,
                                       ChapResponseDigestAlgorithm responseDigestAlgorithm) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.passwordDigestAlgorithm = checkNotNull(passwordDigestAlgorithm);
        this.responseDigestAlgorithm = checkNotNull(responseDigestAlgorithm);
    }

    public <A extends AbstractAuthenticationAction<R>, R extends AbstractAuthenticationResult> void execute(final UserId userId, final String clearTextPassword, final AuthenticationActionFactory<A, R> actionFactory, final AsyncCallback<AuthenticationResponse> callback) {
        dispatchServiceManager.execute(new GetChapSessionAction(userId), new DispatchServiceCallback<GetChapSessionResult>() {
            @Override
            public void handleSuccess(GetChapSessionResult result) {
                Optional<ChapSession> chapSession = result.getChapSession();
                if (chapSession.isPresent()) {
                    constructAndSendResponse(userId, clearTextPassword, chapSession.get(), actionFactory, callback);
                } else {
                    callback.onSuccess(AuthenticationResponse.FAIL);
                }
            }
        });
    }

    private <A extends AbstractAuthenticationAction<R>, R extends AbstractAuthenticationResult> void constructAndSendResponse(UserId userId, String clearTextPassword, ChapSession chapSession, AuthenticationActionFactory<A, R> actionFactory, final AsyncCallback<AuthenticationResponse> callback) {
        SaltedPasswordDigest saltedPasswordDigest = passwordDigestAlgorithm.getDigestOfSaltedPassword(
                clearTextPassword,
                chapSession.getSalt());

        ChapResponse response = responseDigestAlgorithm.getChapResponseDigest(
                chapSession.getChallengeMessage(),
                saltedPasswordDigest);

        sendResponse(userId, chapSession, response, actionFactory, callback);
    }

    private <A extends AbstractAuthenticationAction<R>, R extends AbstractAuthenticationResult> void sendResponse(UserId userId, ChapSession chapSession, ChapResponse chapResponse, AuthenticationActionFactory<A, R> actionFactory, final AsyncCallback<AuthenticationResponse> callback) {
        ChapSessionId chapSessionId = chapSession.getId();
        A action = actionFactory.createAction(chapSessionId, userId, chapResponse);
        dispatchServiceManager.execute(action,
                new DispatchServiceCallback<R>() {
                    @Override
                    public void handleSuccess(R loginResult) {
                        callback.onSuccess(loginResult.getResponse());
                    }
                });
    }


}

