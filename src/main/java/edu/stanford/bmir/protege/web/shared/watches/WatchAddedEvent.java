package edu.stanford.bmir.protege.web.shared.watches;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class WatchAddedEvent extends ProjectEvent<WatchAddedHandler> {

    public static final transient Event.Type<WatchAddedHandler> TYPE = new Event.Type<>();

    private Watch watch;

    /**
     * For serialization only.
     */
    private WatchAddedEvent() {
    }

    /**
     * Creates a {@link WatchAddedEvent}.
     * @param source The id of the project that the watch was added to.  Not {@code null}.
     * @param watch The watch that was added.  Not {@code null}.
     */
    public WatchAddedEvent(ProjectId source, Watch watch) {
        super(source);
        this.watch = watch;
    }

    public Watch getWatch() {
        return watch;
    }

    public UserId getUserId() {
        return watch.getUserId();
    }


    @Override
    public Event.Type<WatchAddedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(WatchAddedHandler handler) {
        handler.handleWatchAdded(this);
    }
}
