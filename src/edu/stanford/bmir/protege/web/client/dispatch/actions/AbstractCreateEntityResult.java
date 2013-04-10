package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public abstract class AbstractCreateEntityResult<E extends OWLEntity> implements Result, HasProjectId, HasEventList<ProjectEvent<?>> {

    private ProjectId projectId;

    private Map<E, String> browserText2EntityMap;

    private EventList<ProjectEvent<?>> eventList;

    /**
     * For serialization purposes only!
     */
    protected AbstractCreateEntityResult() {
    }

    public AbstractCreateEntityResult(Map<E, String> browserText2EntityMap, ProjectId projectId, EventList<ProjectEvent<?>> eventList) {
        this.browserText2EntityMap = new HashMap<E, String>(browserText2EntityMap);
        this.projectId = projectId;
        this.eventList = eventList;
    }

    public Set<String> getBrowserTexts() {
        return new HashSet<String>(browserText2EntityMap.values());
    }

    public Set<E> getEntities() {
        return new HashSet<E>(browserText2EntityMap.keySet());
    }

    public Optional<String> getBrowserText(E entity) {
        return Optional.fromNullable(browserText2EntityMap.get(entity));
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
