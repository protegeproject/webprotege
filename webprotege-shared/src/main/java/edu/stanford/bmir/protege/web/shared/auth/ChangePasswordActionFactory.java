package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class ChangePasswordActionFactory implements AuthenticationActionFactory<ChangePasswordAction, ChangePasswordResult> {

    private final String newPassword;

    private final Provider<Salt> saltProvider;

    public ChangePasswordActionFactory(String newPassword, Provider<Salt> saltProvider) {
        this.newPassword = newPassword;
        this.saltProvider = saltProvider;
    }

    @Override
    public ChangePasswordAction createAction(ChapSessionId sessionId, UserId userId, ChapResponse chapResponse) {
        PasswordDigestAlgorithm passwordDigestAlgorithm = new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider());
        Salt newSalt = saltProvider.get();
        SaltedPasswordDigest saltedPasswordDigest = passwordDigestAlgorithm.getDigestOfSaltedPassword(newPassword, newSalt);
        return new ChangePasswordAction(userId, sessionId, chapResponse, saltedPasswordDigest, newSalt);
    }
}
