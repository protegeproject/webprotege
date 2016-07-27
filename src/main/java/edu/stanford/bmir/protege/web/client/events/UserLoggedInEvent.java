package edu.stanford.bmir.protege.web.client.events;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2013
 */
public class UserLoggedInEvent extends WebProtegeEvent<UserLoggedInHandler> {


    public transient static final Event.Type<UserLoggedInHandler> TYPE = new Event.Type<UserLoggedInHandler>();

    private UserId userId;

    private UserLoggedInEvent() {
    }

    public UserLoggedInEvent(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public Event.Type<UserLoggedInHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserLoggedInHandler handler) {
        handler.handleUserLoggedIn(this);
    }

    @Override
    public int hashCode() {
        return "UserLoggedInEvent".hashCode() + userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof UserLoggedInEvent)) {
            return false;
        }
        UserLoggedInEvent other = (UserLoggedInEvent) obj;
        return this.userId.equals(other.userId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserLoggedInEvent");
        sb.append("(");
        sb.append(userId);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public ProjectId getSource() {
        return null;
    }
}
