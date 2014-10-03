package edu.stanford.bmir.protege.web.client.sel;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 03/10/2014
 */
public class RootEntityChangedEvent extends Event<RootEntityChangedEventHandler> {

    private static final Type<RootEntityChangedEventHandler> TYPE = new Type<RootEntityChangedEventHandler>();

    private final Object eventSource;

    private final Optional<OWLEntityData> previousRoot;

    private final Optional<OWLEntityData> currentRoot;

    /**
     * Constructs the root entity changed event.
     * @param previousRoot The previous root entity.  Not {@code null}.
     * @param currentRoot The current root entity.  Not {@code null}.
     * @param eventSource The source of the change.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public RootEntityChangedEvent(
            Optional<OWLEntityData> previousRoot, Optional<OWLEntityData> currentRoot, Object eventSource) {
        this.previousRoot = checkNotNull(previousRoot);
        this.currentRoot = checkNotNull(currentRoot);
        this.eventSource = checkNotNull(eventSource);
    }

    public static Type<RootEntityChangedEventHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<RootEntityChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RootEntityChangedEventHandler handler) {
        handler.handleRootEntityChanged(this);
    }


    /**
     * Gets the event source.  Not {@code null}.
     * @return The event source.  Not {@code null}.
     */
    public Object getEventSource() {
        return eventSource;
    }

    /**
     * Gets the previous root entity.
     * @return The previous root entity.  Not {@code null}.
     */
    public Optional<OWLEntityData> getPreviousRoot() {
        return previousRoot;
    }

    /**
     * Gets the current root entity.
     * @return The current root entity.  Not {@code null}.
     */
    public Optional<OWLEntityData> getCurrentRoot() {
        return currentRoot;
    }
}
