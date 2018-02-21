package edu.stanford.bmir.protege.web.client.anchor;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 */
public class AnchorClickedEvent extends GwtEvent<AnchorClickedHandler> {

    public static final Type<AnchorClickedHandler> TYPE = new Type<AnchorClickedHandler>();

    private HasAnchorClickedHandlers source;

    public AnchorClickedEvent(HasAnchorClickedHandlers source) {
        this.source = source;
    }

    @Override
    public Type<AnchorClickedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnchorClickedHandler handler) {
        handler.handleAnchorClicked(this);
    }
}
