package edu.stanford.bmir.protege.web.server.auth;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage;
import edu.stanford.bmir.protege.web.shared.auth.ChapSessionId;
import edu.stanford.bmir.protege.web.shared.auth.Md5MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
@Module
public class AuthenticationModule {

    private static final long MAX_SESSION_DURATION_MS = 2 * 60 * 1000;

    @Provides
    public MessageDigestAlgorithm provide(Md5MessageDigestAlgorithm algorithm) {
        return algorithm;
    }

    @Provides
    @ChapSessionMaxDuration
    public long provideMaxSessionDuration() {
        return MAX_SESSION_DURATION_MS;
    }

    @ApplicationSingleton
    @Provides
    public ChapSessionManager provideChapSessionManager(ChapSessionFactory factory,
                                                        @ChapSessionMaxDuration long maxSessionDuration) {
        return new ChapSessionManager(factory, maxSessionDuration);
    }

    @Provides
    public ChapSessionId provideChapSessionId(ChapSessionIdProvider provider) {
        return provider.get();
    }

    @Provides
    public ChallengeMessage provideChallengeMessage(ChallengeMessageProvider provider) {
        return provider.get();
    }
}
