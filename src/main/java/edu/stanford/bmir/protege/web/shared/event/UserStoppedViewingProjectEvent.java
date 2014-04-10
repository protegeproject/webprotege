package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/03/2013
 */
public class UserStoppedViewingProjectEvent extends ProjectEvent<UserStoppedViewingProjectHandler> implements HasUserId {

    public transient static final Type<UserStoppedViewingProjectHandler> TYPE = new Type<UserStoppedViewingProjectHandler>();

    private UserId userId;

    public UserStoppedViewingProjectEvent(ProjectId source, UserId userId) {
        super(source);
        this.userId = userId;
    }

    /**
     * For serialization only
     */
    private UserStoppedViewingProjectEvent() {
    }

    @Override
    public Type<UserStoppedViewingProjectHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserStoppedViewingProjectHandler handler) {
        handler.handleUserStoppedViewingProject(this);
    }

    @Override
    public UserId getUserId() {
        return userId;
    }
}
