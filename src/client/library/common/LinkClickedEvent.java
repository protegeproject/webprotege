package edu.stanford.bmir.protege.web.client.library.common;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
public class LinkClickedEvent extends Event<LinkClickedHandler> {

    public static final Type<LinkClickedHandler> TYPE = new Type<LinkClickedHandler>();

    private OWLPrimitiveData clickedObject;

    public LinkClickedEvent(OWLPrimitiveData clickedObject) {
        this.clickedObject = clickedObject;
    }

    public OWLPrimitiveData getClickedObject() {
        return clickedObject;
    }

    /**
     * Returns the {@link com.google.web.bindery.event.shared.Event.Type} used to register this event, allowing an
     * {@link com.google.web.bindery.event.shared.EventBus} to find handlers of the appropriate class.
     * @return the type
     */
    @Override
    public Type<LinkClickedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Implemented by subclasses to to invoke their handlers in a type safe
     * manner. Intended to be called by {@link com.google.web.bindery.event.shared.EventBus#fireEvent(
     *com.google.web.bindery.event.shared.Event)} or
     * {@link com.google.web.bindery.event.shared.EventBus#fireEventFromSource(com.google.web.bindery.event.shared.Event,
     * Object)}.
     * @param handler handler
     * @see com.google.web.bindery.event.shared.EventBus#dispatchEvent(com.google.web.bindery.event.shared.Event,
     *      Object)
     */
    @Override
    protected void dispatch(LinkClickedHandler handler) {
        handler.linkClicked(this);
    }
}
