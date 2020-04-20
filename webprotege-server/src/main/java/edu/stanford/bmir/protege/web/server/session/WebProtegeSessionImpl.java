package edu.stanford.bmir.protege.web.server.session;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class WebProtegeSessionImpl implements WebProtegeSession {

    private final UserId userId;

    @Inject
    public WebProtegeSessionImpl(UserId userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("WebProtegeSession")
                          .addValue(userId)
                          .toString();
    }

    @Override
    public UserId getUserInSession() {
        return userId;
    }

    @Override
    public void clearUserInSession() {
    }
}
