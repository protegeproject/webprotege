package edu.stanford.bmir.protege.web.server.auth;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.stanford.bmir.protege.web.shared.auth.ChapSession;
import edu.stanford.bmir.protege.web.shared.auth.ChapSessionId;
import edu.stanford.bmir.protege.web.shared.auth.Salt;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
@Singleton
public class ChapSessionManager {

    private final Cache<ChapSessionId, ChapSession> messageCache;

    private final ChapSessionFactory chapSessionFactory;

    @Inject
    public ChapSessionManager(ChapSessionFactory chapSessionFactory,
                              @ChapSessionMaxDuration long maxSessionDurationInMilliseconds) {
        this.chapSessionFactory = checkNotNull(chapSessionFactory);
        messageCache = CacheBuilder.newBuilder()
                .expireAfterWrite(maxSessionDurationInMilliseconds, TimeUnit.MILLISECONDS)
                .build();
    }

    public Optional<ChapSession> retrieveChallengeMessage(ChapSessionId id) {
        return Optional.fromNullable(messageCache.getIfPresent(id));
    }

    public ChapSession getSession(Salt salt) {
        ChapSession message = chapSessionFactory.getChapSession(salt);
        messageCache.put(message.getId(), message);
        return message;
    }
}
