package edu.stanford.bmir.protege.web.client.ui.library.common;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public class EditingFinishedEvent<T> extends GwtEvent<EditingFinishedHandler<T>> {

    public static final Type<EditingFinishedHandler<?>> TYPE = new Type<EditingFinishedHandler<?>>();

    private T value;

    public EditingFinishedEvent(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Type<EditingFinishedHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(EditingFinishedHandler<T> handler) {
        handler.editingFinished(this);
    }

    public T getValue() {
        return value;
    }
}
