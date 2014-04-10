package edu.stanford.bmir.protege.web.shared.event;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.HasChangedValue;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class OntologyBrowserTextChangedEvent extends Event<OntologyBrowserTextChangedEventHandler> implements Serializable, HasChangedValue<String> {

    public static final transient Type<OntologyBrowserTextChangedEventHandler> TYPE = new Type<OntologyBrowserTextChangedEventHandler>();

    private OWLOntologyID ontologyID;

    private String oldValue;

    private String newValue;

    /**
     * Constructs an OntologyBrowserTextChangedEvent.
     * @param ontologyID The id of the ontology whose browser text has changed.  Not {@code null}.
     * @param oldValue The old browser text.  Not {@code null}.  May be empty.
     * @param newValue The new browser text.  Not {@code null}.  May be empty.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public OntologyBrowserTextChangedEvent(OWLOntologyID ontologyID, String oldValue, String newValue) {
        checkNotNull(ontologyID);
        checkNotNull(oldValue);
        checkNotNull(newValue);
        this.ontologyID = ontologyID;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Serialization purposes only
     */
    private OntologyBrowserTextChangedEvent() {

    }

    public OWLOntologyID getOntologyID() {
        return ontologyID;
    }

    @Override
    public Optional<String> getOldValue() {
        return Optional.of(oldValue);
    }

    @Override
    public Optional<String> getNewValue() {
        return Optional.of(newValue);
    }

    /**
     * Returns the {@link com.google.web.bindery.event.shared.Event.Type} used to register this event, allowing an
     * {@link com.google.web.bindery.event.shared.EventBus} to find handlers of the appropriate class.
     * @return the type
     */
    @Override
    public Type<OntologyBrowserTextChangedEventHandler> getAssociatedType() {
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
    protected void dispatch(OntologyBrowserTextChangedEventHandler handler) {
        handler.ontologyBrowserTextChanged(this);
    }
}
