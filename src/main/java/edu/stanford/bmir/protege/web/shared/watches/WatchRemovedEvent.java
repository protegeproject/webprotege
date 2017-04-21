package edu.stanford.bmir.protege.web.shared.watches;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class WatchRemovedEvent extends ProjectEvent<WatchRemovedHandler> implements HasUserId {

    public transient static final Event.Type<WatchRemovedHandler> TYPE = new Event.Type<WatchRemovedHandler>();

    private Watch watch;

    public WatchRemovedEvent(ProjectId source, Watch watch) {
        super(source);
        this.watch = watch;
    }

    private WatchRemovedEvent() {
    }

    @Override
    public Event.Type<WatchRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(WatchRemovedHandler handler) {
        handler.handleWatchRemoved(this);
    }

    public Watch getWatch() {
        return watch;
    }

    public UserId getUserId() {
        return watch.getUserId();
    }

    @Override
    public int hashCode() {
        return "WatchRemovedEvent".hashCode() + getProjectId().hashCode() + watch.hashCode();
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
        return this.getProjectId().equals(other.getProjectId()) && this.getWatch().equals(other.getWatch());
    }
}
