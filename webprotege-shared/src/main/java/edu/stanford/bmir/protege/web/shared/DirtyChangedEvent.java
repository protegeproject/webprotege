package edu.stanford.bmir.protege.web.shared;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public class DirtyChangedEvent extends GwtEvent<DirtyChangedHandler> {

    public static final Type<DirtyChangedHandler> TYPE = new Type<DirtyChangedHandler>();

    @Override
    public Type<DirtyChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DirtyChangedHandler handler) {
        handler.handleDirtyChanged(this);
    }
}
