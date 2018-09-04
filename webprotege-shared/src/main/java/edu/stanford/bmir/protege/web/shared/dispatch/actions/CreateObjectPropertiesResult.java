package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertiesResult extends CreateEntitiesInHierarchyResult<OWLObjectProperty> {

    public CreateObjectPropertiesResult(@Nonnull ProjectId projectId,
                                        @Nonnull ImmutableCollection<EntityNode> entities,
                                        EventList<ProjectEvent<?>> eventList) {
        super(projectId, entities, eventList);
    }

    @GwtSerializationConstructor
    private CreateObjectPropertiesResult() {
    }

}
