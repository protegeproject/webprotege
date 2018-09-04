package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsResult extends AbstractCreateEntityResult<OWLNamedIndividual> {

    private CreateNamedIndividualsResult() {
    }

    public CreateNamedIndividualsResult(@Nonnull ProjectId projectId, @Nonnull EventList<ProjectEvent<?>> eventList, ImmutableCollection<EntityNode> entities) {
        super(projectId, eventList, entities);
    }
}
