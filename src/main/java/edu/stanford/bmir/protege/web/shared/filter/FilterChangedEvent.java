package edu.stanford.bmir.protege.web.shared.filter;

import com.google.web.bindery.event.shared.Event;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public class FilterChangedEvent extends Event<FilterChangedHandler> {

    private static final Type<FilterChangedHandler> TYPE = new Type<>();

    private final Filter filter;

    public FilterChangedEvent(Filter filter) {
        this.filter = checkNotNull(filter);
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public Type<FilterChangedHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<FilterChangedHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FilterChangedHandler handler) {
        handler.handleFilterChanged(this);
    }
}
