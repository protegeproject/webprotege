package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class WatchRemovedEvent extends ProjectEvent<WatchRemovedHandler> implements HasUserId {

    public transient static final Type<WatchRemovedHandler> TYPE = new Type<WatchRemovedHandler>();

    private Watch<?> watch;

    private UserId userId;

    public WatchRemovedEvent(ProjectId source, Watch<?> watch, UserId userId) {
        super(source);
        this.watch = watch;
        this.userId = userId;
    }

    private WatchRemovedEvent() {
    }

    @Override
    public Type<WatchRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(WatchRemovedHandler handler) {
        handler.handleWatchRemoved(this);
    }

    public Watch<?> getWatch() {
        return watch;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return "WatchRemovedEvent".hashCode() + getProjectId().hashCode() + watch.hashCode() + userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof WatchRemovedEvent)) {
            return false;
        }
        WatchRemovedEvent other = (WatchRemovedEvent) obj;
        return this.getProjectId().equals(other.getProjectId()) && this.getWatch().equals(other.getWatch()) && this.getUserId().equals(other.getUserId());
    }
}
