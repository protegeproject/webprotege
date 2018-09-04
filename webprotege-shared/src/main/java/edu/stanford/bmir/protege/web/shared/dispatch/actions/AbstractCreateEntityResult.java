package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public abstract class AbstractCreateEntityResult<E extends OWLEntity> implements Result, HasProjectId, HasEventList<ProjectEvent<?>> {

    private ProjectId projectId;

    private EventList<ProjectEvent<?>> eventList;

    private ImmutableCollection<EntityNode> entities;

    public AbstractCreateEntityResult(@Nonnull ProjectId projectId,
                                      @Nonnull EventList<ProjectEvent<?>> eventList,
                                      ImmutableCollection<EntityNode> entities) {
        this.projectId = checkNotNull(projectId);
        this.eventList = checkNotNull(eventList);
        this.entities = checkNotNull(entities);
    }

    @GwtSerializationConstructor
    protected AbstractCreateEntityResult() {
    }


    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }

    public ImmutableCollection<EntityNode> getEntities() {
        return entities;
    }
}
