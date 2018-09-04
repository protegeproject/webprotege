package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public abstract class CreateEntitiesInHierarchyResult<E extends OWLEntity> extends AbstractCreateEntityResult<E> {

    public CreateEntitiesInHierarchyResult(@Nonnull ProjectId projectId,
                                           @Nonnull ImmutableCollection<EntityNode> entities,
                                           @Nonnull EventList<ProjectEvent<?>> eventList) {
        super(projectId, eventList, entities);
    }

    @GwtSerializationConstructor
    protected CreateEntitiesInHierarchyResult() {
    }
}
