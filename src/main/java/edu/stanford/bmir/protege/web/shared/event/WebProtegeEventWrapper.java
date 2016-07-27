package edu.stanford.bmir.protege.web.shared.event;

import com.google.web.bindery.event.shared.Event;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Jul 16
 */
public class WebProtegeEventWrapper extends Event<WebProtegeEventWrapperHandler> {

    private final WebProtegeEvent<?> event;

    public WebProtegeEventWrapper(WebProtegeEvent<?> event) {
        this.event = event;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Type<WebProtegeEventWrapperHandler> getAssociatedType() {
        // Really the type of our delegate event
        return (Type<WebProtegeEventWrapperHandler>) event.getAssociatedType();
    }

    @Override
    protected void dispatch(WebProtegeEventWrapperHandler webProtegeEventWrapperHandler) {

    }
}
