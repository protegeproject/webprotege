package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class CreateClassesResult extends CreateEntitiesInHierarchyResult<OWLClass> {

    @GwtSerializationConstructor
    private CreateClassesResult() {
    }

    public CreateClassesResult(ProjectId projectId,
                               ImmutableSet<EntityNode> classes,
                               EventList<ProjectEvent<?>> eventList) {
        super(projectId, classes, eventList);
    }
}
