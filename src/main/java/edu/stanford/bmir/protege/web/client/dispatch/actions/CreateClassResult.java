package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class CreateClassResult implements GetObjectResult<OWLClassData>, HasEventList<ProjectEvent<?>> {

    private ObjectPath<OWLClass> pathToRoot;

    private OWLClassData cls;

    private EventList<ProjectEvent<?>> eventList;

    private CreateClassResult() {
    }

    /**
     * Constructs a {@link CreateClassResult}.
     * @param cls The class that was created.  Not {@code null}.
     * @param pathToRoot The path to the root (owl:Thing).  Not {@code null}.
     */
    public CreateClassResult(OWLClassData cls, ObjectPath<OWLClass> pathToRoot, EventList<ProjectEvent<?>> eventList) {
        if(pathToRoot.isEmpty()) {
            throw new IllegalArgumentException("pathToRoot must not be empty");
        }
        this.cls = checkNotNull(cls);
        this.pathToRoot = checkNotNull(pathToRoot);
        this.eventList = eventList;
    }

    @Override
    public OWLClassData getObject() {
        return cls;
    }
    
    /**
     * Gets the path to root, which is non-empty.
     * @return A non-empty path to root.
     */
    public ObjectPath<OWLClass> getPathToRoot() {
        return pathToRoot;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
