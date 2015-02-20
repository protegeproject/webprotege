package edu.stanford.bmir.protege.web.shared.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.auth.PasswordDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class CreateUserAccountExecutor {

    private DispatchServiceManager dispatchServiceManager;

    private PasswordDigestAlgorithm passwordDigestAlgorithm;

    private Provider<Salt> saltProvider;

    @Inject
    public CreateUserAccountExecutor(DispatchServiceManager dispatchServiceManager, PasswordDigestAlgorithm passwordDigestAlgorithm, Provider<Salt> saltProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.passwordDigestAlgorithm = passwordDigestAlgorithm;
        this.saltProvider = saltProvider;
    }

    public void execute(UserId userId, EmailAddress emailAddress, String clearTextPassword, AsyncCallback<CreateUserAccountResult> callback) {
        Salt salt = saltProvider.get();
        SaltedPasswordDigest saltedPasswordDigest = passwordDigestAlgorithm.getDigestOfSaltedPassword(clearTextPassword, salt);
        dispatchServiceManager.execute(new CreateUserAccountAction(userId, emailAddress, saltedPasswordDigest, salt), callback);
    }
}
