package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class ChapProtocolExecutor {

    private final DispatchServiceManager dispatchServiceManager;

    private final PasswordDigestAlgorithm passwordDigestAlgorithm;

    private final ChapResponseDigestAlgorithm responseDigestAlgorithm;

    @Inject
    public ChapProtocolExecutor(DispatchServiceManager dispatchServiceManager,
                                PasswordDigestAlgorithm passwordDigestAlgorithm,
                                ChapResponseDigestAlgorithm responseDigestAlgorithm) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.passwordDigestAlgorithm = checkNotNull(passwordDigestAlgorithm);
        this.responseDigestAlgorithm = checkNotNull(responseDigestAlgorithm);
    }

    public void execute(final UserId userId, final String clearTextPassword, final AsyncCallback<AuthenticationResponse> callback) {
        dispatchServiceManager.execute(new GetChapSessionAction(userId), new AbstractWebProtegeAsyncCallback<GetChapSessionResult>() {
            @Override
            public void onSuccess(GetChapSessionResult result) {
                Optional<ChapSession> chapSession = result.getChapSession();
                if (chapSession.isPresent()) {
                    constructAndSendResponse(userId, clearTextPassword, chapSession.get(), callback);
                } else {
                    callback.onSuccess(AuthenticationResponse.FAIL);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                callback.onFailure(caught);
            }
        });
    }

    private void constructAndSendResponse(UserId userId, String clearTextPassword, ChapSession chapSession, final AsyncCallback<AuthenticationResponse> callback) {
        SaltedPasswordDigest saltedPasswordDigest = passwordDigestAlgorithm.getDigestOfSaltedPassword(
                clearTextPassword,
                chapSession.getSalt());

        ChapResponse response = responseDigestAlgorithm.getChapResponseDigest(
                chapSession.getChallengeMessage(),
                saltedPasswordDigest);

        sendResponse(userId, chapSession, response, callback);
    }

    private void sendResponse(UserId userId, ChapSession chapSession, ChapResponse chapResponse, final AsyncCallback<AuthenticationResponse> callback) {
        ChapSessionId chapSessionId = chapSession.getId();
        dispatchServiceManager.execute(new PerformLoginAction(userId, chapSessionId, chapResponse),
                new AbstractWebProtegeAsyncCallback<PerformLoginResult>() {
                    @Override
                    public void onSuccess(PerformLoginResult loginResult) {
                        callback.onSuccess(loginResult.getResponse());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        callback.onFailure(caught);
                    }
                });
    }
}
