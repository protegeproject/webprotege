package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public abstract class CreateEntitiesInHierarchyResult<E extends OWLEntity> extends AbstractCreateEntityResult<E> {

    private ImmutableCollection<E> entities;

    public CreateEntitiesInHierarchyResult(@Nonnull ProjectId projectId,
                                           @Nonnull ImmutableCollection<E> entities,
                                           @Nonnull EventList<ProjectEvent<?>> eventList) {
        super(projectId, eventList);
        this.entities = entities;
    }

    @GwtSerializationConstructor
    protected CreateEntitiesInHierarchyResult() {
    }

    @Nonnull
    public ImmutableCollection<E> getEntities() {
        return entities;
    }
}
