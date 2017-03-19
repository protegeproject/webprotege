package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.server.user.UserActivityManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
@ApplicationSingleton
public class WebProtegeSessionListener implements HttpSessionListener {

    private final UserActivityManager userActivityManager;

    private final WebProtegeLogger logger;

    @Inject
    public WebProtegeSessionListener(@Nonnull UserActivityManager userActivityManager,
                                     @Nonnull WebProtegeLogger logger) {
        this.userActivityManager = checkNotNull(userActivityManager);
        this.logger = logger;
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        WebProtegeSession session = new WebProtegeSessionImpl(httpSessionEvent.getSession());
        UserId userId = session.getUserInSession();
        logger.info("User session expired for %s", userId);
        userActivityManager.setLastLogout(userId, System.currentTimeMillis());
    }
}
