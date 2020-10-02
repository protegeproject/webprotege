package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-30
 */
public class CreateEntityFromFormDataResult extends AbstractCreateEntityResult<OWLEntity> {

    public CreateEntityFromFormDataResult(@Nonnull ProjectId projectId,
                                          @Nonnull EventList<ProjectEvent<?>> eventList,
                                          ImmutableCollection<EntityNode> entities) {
        super(projectId, eventList, entities);
    }

    @GwtSerializationConstructor
    private CreateEntityFromFormDataResult() {
    }
}
