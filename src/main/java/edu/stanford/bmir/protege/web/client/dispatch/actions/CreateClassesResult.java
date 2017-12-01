package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.*;

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

    private ImmutableList<OWLClassData> createdClasses;

    private EventList<ProjectEvent<?>> eventList;

    private CreateClassesResult() {
    }

    public CreateClassesResult(@Nonnull ObjectPath<OWLClass> superClassPathToRoot,
                               @Nonnull List<OWLClassData> createdClasses,
                               @Nonnull EventList<ProjectEvent<?>> eventList) {
        this.superClassPathToRoot = checkNotNull(superClassPathToRoot);
        this.createdClasses = ImmutableList.copyOf(createdClasses);
        this.eventList = checkNotNull(eventList);
    }

    public ObjectPath<OWLClass> getSuperClassPathToRoot() {
        return superClassPathToRoot;
    }

    public List<OWLClassData> getCreatedClasses() {
        return createdClasses;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
