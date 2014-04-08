package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
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
public class CreateClassResult extends RenderableGetObjectResult<OWLClass> implements Result, HasEventList<ProjectEvent<?>> {

    private ObjectPath<OWLClass> pathToRoot;

    private EventList<ProjectEvent<?>> eventList;

    private CreateClassResult() {
    }

    /**
     * Constructs a {@link CreateClassResult}.
     * @param object The class that was created.  Not {@code null}.
     * @param pathToRoot The path to the root (owl:Thing).  Not {@code null}.
     * @param browserTextMap A browse
     */
    public CreateClassResult(OWLClass object, ObjectPath<OWLClass> pathToRoot, BrowserTextMap browserTextMap, EventList<ProjectEvent<?>> eventList) {
        super(checkNotNull(object), checkNotNull(browserTextMap));
        if(pathToRoot.isEmpty()) {
            throw new IllegalArgumentException("pathToRoot must not be empty");
        }
        this.pathToRoot = checkNotNull(pathToRoot);
        this.eventList = eventList;
    }


    public List<OWLClass> getSuperClasses() {
        return Arrays.asList(pathToRoot.get(0));
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
