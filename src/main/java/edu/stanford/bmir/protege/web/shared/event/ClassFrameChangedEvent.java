package edu.stanford.bmir.protege.web.shared.event;


import com.google.common.base.Objects;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class ClassFrameChangedEvent extends EntityFrameChangedEvent<OWLClass, ClassFrameChangedEventHandler> {

    public transient static final Event.Type<ClassFrameChangedEventHandler> TYPE = new Event.Type<ClassFrameChangedEventHandler>();

    public ClassFrameChangedEvent(OWLClass entity, ProjectId projectId, UserId userId) {
        super(entity, projectId, userId);
    }

    private ClassFrameChangedEvent() {
        super();
    }

    /**
     * Returns the {@link com.google.web.bindery.event.shared.Event.Type} used to register this event, allowing an
     * {@link com.google.web.bindery.event.shared.EventBus} to find handlers of the appropriate class.
     * @return the type
     */
    @Override
    public Event.Type<ClassFrameChangedEventHandler> getAssociatedType() {
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
    protected void dispatch(ClassFrameChangedEventHandler handler) {
        handler.classFrameChanged(this);
    }

    @Override
    public int hashCode() {
        return "ClassFrameChangedEvent".hashCode() + getEntity().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ClassFrameChangedEvent)) {
            return false;
        }
        ClassFrameChangedEvent other = (ClassFrameChangedEvent) obj;
        return this.getEntity().equals(other.getEntity());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("ClassFrameChangedEvent")
                .addValue(getEntity()).toString();
    }
}
