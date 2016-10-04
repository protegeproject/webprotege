package edu.stanford.bmir.protege.web.server.auth;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.shared.auth.*;

import javax.inject.Singleton;

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

    @Singleton
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

    @Provides
    public ChapSessionFactory provideChapSessionFactory(ChapSessionId chapSessionId, ChallengeMessage challengeMessage) {
        return new ChapSessionFactory(chapSessionId, challengeMessage);
    }
}
