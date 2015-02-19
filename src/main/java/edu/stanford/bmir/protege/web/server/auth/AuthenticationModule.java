package edu.stanford.bmir.protege.web.server.auth;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import edu.stanford.bmir.protege.web.shared.auth.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class AuthenticationModule extends AbstractModule {

    private static final long MAX_SESSION_DURATION_MS = 2 * 60 * 1000;

    @Override
    protected void configure() {
        bind(MessageDigestAlgorithm.class).to(Md5MessageDigestAlgorithm.class);

        // Chap session
        bind(ChapSessionManager.class).asEagerSingleton();
        bindConstant().annotatedWith(ChapSessionMaxDuration.class).to(MAX_SESSION_DURATION_MS);

        // Creating ChapData
        bind(ChapSessionId.class).toProvider(ChapSessionIdProvider.class);
        bind(ChallengeMessage.class).toProvider(ChallengeMessageProvider.class);
        install(new FactoryModuleBuilder().build(ChapSessionFactory.class));

    }
}
