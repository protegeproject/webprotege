package edu.stanford.bmir.protege.web.server.session;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class WebProtegeSessionImpl implements WebProtegeSession {

    private final HttpSession httpSession;

    @Inject
    public WebProtegeSessionImpl(HttpSession httpSession) {
        this.httpSession = checkNotNull(httpSession);
    }


    @Override
    public void removeAttribute(WebProtegeSessionAttribute<?> attribute) {
        httpSession.removeAttribute(checkNotNull(attribute.getAttributeName()));
    }

    @Override
    public <T> void setAttribute(WebProtegeSessionAttribute<T> attribute, T value) {
        httpSession.setAttribute(checkNotNull(attribute.getAttributeName()), checkNotNull(value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAttribute(WebProtegeSessionAttribute<T> attribute) {
        T value = (T) httpSession.getAttribute(attribute.getAttributeName());
        return Optional.ofNullable(value);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("WebProtegeSession")
                .addValue(httpSession)
                .toString();
    }

    @Override
    public UserId getUserInSession() {
        return getAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER).orElse(UserId.getGuest());
    }

    @Override
    public void setUserInSession(UserId userId) {
        if (!userId.isGuest()) {
            setAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER, checkNotNull(userId));
        }
    }

    @Override
    public void clearUserInSession() {
        removeAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER);
    }
}
