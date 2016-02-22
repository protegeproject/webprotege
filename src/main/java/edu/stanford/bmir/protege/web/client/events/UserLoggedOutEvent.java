package edu.stanford.bmir.protege.web.client.events;

import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2013
 */
public class UserLoggedOutEvent extends WebProtegeEvent<UserLoggedOutHandler> {

    public static final transient Type<UserLoggedOutHandler> TYPE = new Type<UserLoggedOutHandler>();

    private UserId userId;

    private UserLoggedOutEvent() {
    }

    public UserLoggedOutEvent(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public Type<UserLoggedOutHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserLoggedOutHandler handler) {
        handler.handleUserLoggedOut(this);
    }
}
