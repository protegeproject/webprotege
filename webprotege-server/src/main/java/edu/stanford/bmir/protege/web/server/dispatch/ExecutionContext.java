package edu.stanford.bmir.protege.web.server.dispatch;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     Describes the context in which an action is being executed.
 * </p>
 */
public class ExecutionContext implements HasUserId {

    private WebProtegeSession session;

    /**
     * Creates an ExecutionContext.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    public ExecutionContext(WebProtegeSession session) {
        this.session = checkNotNull(session);
    }

    /**
     * Gets the {@link UserId} in this execution context.
     * @return The {@link UserId}.  Not {@code null}.  If the associated session (see {@link #getSession()})
     * does not have a logged in user then the {@link edu.stanford.bmir.protege.web.shared.user.UserId}
     * equal to the guest user is returned.
     */
    public UserId getUserId() {
        return session.getUserInSession();
    }

    /**
     * Gets the WebProtegeSession that the action is executed in.
     * @return The WebProtegeSession.  Not {@code null}.
     */
    public WebProtegeSession getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        return "ExecutionContext".hashCode();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ExecutionContext")
                          .add("session", session)
                          .toString();
    }
}
