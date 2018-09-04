package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateAnnotationPropertiesResult extends CreateEntitiesInHierarchyResult<OWLAnnotationProperty> {

    public CreateAnnotationPropertiesResult(@Nonnull ProjectId projectId,
                                            @Nonnull ImmutableSet<EntityNode> annotationProperties,
                                            @Nonnull EventList<ProjectEvent<?>> eventList) {
        super(projectId, annotationProperties, eventList);
    }

    private CreateAnnotationPropertiesResult() {
    }
}
