package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.servlet.http.HttpSession;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public class RequestContext {

    private UserId userId;

    /**
     * Constructs a {@link RequestContext} for the user specified by {@code userId}.
     * @param userId The {@link UserId}.  Not {@code null}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    public RequestContext(UserId userId, HttpSession session) {
        this.userId = checkNotNull(userId, "userId must not be null");
    }

    /**
     * Gets the {@link UserId} for this request.
     * @return The {@link UserId}.  Not {@code null}.
     */
    public UserId getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return "RequestContext".hashCode() + userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof RequestContext)) {
            return false;
        }
        RequestContext other = (RequestContext) obj;
        return this.userId.equals(other.userId);
    }



}
