package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateEntityFromFormDataResult;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.GetEntityCreationFormsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 5 Dec 2017
 */
public class CreateEntityPresenter {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DefaultCreateEntitiesPresenter defaultCreateEntitiesPresenter;

    @Nonnull
    private final CreateEntityFormPresenter createEntityFormPresenter;

    @Inject
    public CreateEntityPresenter(@Nonnull DispatchServiceManager dispatch,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull DefaultCreateEntitiesPresenter defaultCreateEntitiesPresenter,
                                 @Nonnull CreateEntityFormPresenter createEntityFormPresenter) {
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
        this.defaultCreateEntitiesPresenter = checkNotNull(defaultCreateEntitiesPresenter);
        this.createEntityFormPresenter = checkNotNull(createEntityFormPresenter);
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               @Nonnull Optional<? extends OWLEntity> parentEntity,
                               @Nonnull EntitiesCreatedHandler entitiesCreatedHandler) {
        parentEntity.ifPresent(owlEntity -> dispatch.execute(new GetEntityCreationFormsAction(projectId,
                                                                                              owlEntity,
                                                                                              entityType), result -> {
            handleEntityCreationFormsResult(entityType,
                                            parentEntity,
                                            entitiesCreatedHandler,
                                            result.getFormDescriptors());
        }));


    }

    private void handleEntityCreationFormsResult(@Nonnull EntityType<?> entityType,
                                                 @Nonnull Optional<? extends OWLEntity> parentEntity,
                                                 @Nonnull EntitiesCreatedHandler entitiesCreatedHandler,
                                                 @Nonnull ImmutableList<FormDescriptorDto> createEntityForms) {
        if (createEntityForms.isEmpty()) {
            defaultCreateEntitiesPresenter.createEntities(entityType,
                                                          parentEntity,
                                                          entitiesCreatedHandler);
        }
        else {
            createEntityFormPresenter.createEntities(entityType,
                                                     parentEntity,
                                                     entitiesCreatedHandler,
                                                     createEntityForms);
        }
    }

    public interface EntitiesCreatedHandler {

        void handleEntitiesCreated(ImmutableCollection<EntityNode> entities);
    }
}
