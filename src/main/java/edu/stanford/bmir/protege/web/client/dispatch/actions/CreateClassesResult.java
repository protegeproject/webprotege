package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class CreateClassesResult implements Result, HasEventList<ProjectEvent<?>> {

    private ObjectPath<OWLClass> superClassPathToRoot;

    private Set<OWLClassData> createdClasses = new HashSet<>();

    private EventList<ProjectEvent<?>> eventList;

    private CreateClassesResult() {
    }

    public CreateClassesResult(ObjectPath<OWLClass> superClassPathToRoot, Set<OWLClassData> createdClasses, EventList<ProjectEvent<?>> eventList) {
        this.superClassPathToRoot = checkNotNull(superClassPathToRoot);
        this.createdClasses = new HashSet<>(createdClasses);
        this.eventList = checkNotNull(eventList);
    }

    public ObjectPath<OWLClass> getSuperClassPathToRoot() {
        return superClassPathToRoot;
    }

    public Set<OWLClassData> getCreatedClasses() {
        return new HashSet<>(createdClasses);
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
