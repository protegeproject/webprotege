package edu.stanford.bmir.protege.web.shared.event;


import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class UserStartingViewingProjectEvent extends ProjectEvent<UserStartedViewingProjectHandler> implements HasUserId {

    public transient static final Event.Type<UserStartedViewingProjectHandler> TYPE = new Event.Type<UserStartedViewingProjectHandler>();


    private UserId userId;

    public UserStartingViewingProjectEvent(ProjectId source, UserId userId) {
        super(source);
        this.userId = userId;
    }

    private UserStartingViewingProjectEvent() {

    }

    @Override
    public Event.Type<UserStartedViewingProjectHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserStartedViewingProjectHandler handler) {
        handler.handleUserStartedViewingProject(this);
    }

    @Override
    public UserId getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserStartingViewingProjectEvent");
        sb.append("(");
        sb.append(userId);
        sb.append(" ");
        sb.append(getSource());
        sb.append(")");
        return sb.toString();
    }
}
