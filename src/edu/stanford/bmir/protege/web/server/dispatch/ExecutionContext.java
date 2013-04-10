package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.HasUserId;

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

    private UserId userId;

    /**
     * Creates an ExecutionContext.
     * @param userId The userId for the execution context.  Not {@code null}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    public ExecutionContext(UserId userId) {
        this.userId = checkNotNull(userId);
    }

    /**
     * Gets the {@link UserId} in this execution context.
     * @return The {@link UserId}.  Not {@code null}.
     */
    public UserId getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return "ExecutionContext".hashCode() + userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ExecutionContext)) {
            return false;
        }
        ExecutionContext other = (ExecutionContext) obj;
        return this.userId.equals(other.userId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExecutionContext");
        sb.append("(");
        sb.append(userId);
        sb.append(")");
        return sb.toString();
    }
}
