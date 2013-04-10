package edu.stanford.bmir.protege.web.client.ui.library.common;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public class EditingCancelledEvent extends GwtEvent<EditingCancelledHandler> {

    public static final Type<EditingCancelledHandler> TYPE = new Type<EditingCancelledHandler>();

    @Override
    public Type<EditingCancelledHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditingCancelledHandler handler) {
        handler.editingCancelled(this);
    }
}
