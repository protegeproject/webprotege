package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class OntologyFrameChangedEvent extends ProjectEvent<OntologyFrameChangedEventHandler> implements Serializable {

    public static final transient Type<OntologyFrameChangedEventHandler> TYPE = new Type<OntologyFrameChangedEventHandler>();

    private OWLOntologyID ontologyID;

    public OntologyFrameChangedEvent(OWLOntologyID ontologyID, ProjectId projectId) {
        super(projectId);
        this.ontologyID = ontologyID;
    }

    private OntologyFrameChangedEvent() {

    }


    public OWLOntologyID getOntologyID() {
        return ontologyID;
    }

    /**
     * Returns the {@link com.google.web.bindery.event.shared.Event.Type} used to register this event, allowing an
     * {@link com.google.web.bindery.event.shared.EventBus} to find handlers of the appropriate class.
     * @return the type
     */
    @Override
    public Type<OntologyFrameChangedEventHandler> getAssociatedType() {
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
    protected void dispatch(OntologyFrameChangedEventHandler handler) {
        handler.ontologyFrameChanged(this);
    }
}
