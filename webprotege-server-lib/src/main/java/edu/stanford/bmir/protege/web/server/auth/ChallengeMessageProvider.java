package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Random;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class ChallengeMessageProvider implements Provider<ChallengeMessage> {

    private static final int LENGTH = 8;

    @Inject
    public ChallengeMessageProvider() {
    }

    @Override
    public ChallengeMessage get() {
        byte [] bytes = new byte[LENGTH];
        Random random = new Random();
        random.nextBytes(bytes);
        return new ChallengeMessage(bytes);
    }
}
