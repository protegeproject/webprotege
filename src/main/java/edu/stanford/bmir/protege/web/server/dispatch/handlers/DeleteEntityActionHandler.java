package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.crud.DeleteEntityChangeListGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
@ProjectSingleton
public class DeleteEntityActionHandler extends AbstractProjectChangeHandler<OWLEntity, DeleteEntityAction, DeleteEntityResult> {

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public DeleteEntityActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                     @Nonnull HasApplyChanges applyChanges,
                                     @Nonnull RenderingManager renderingManager,
                                     @Nonnull @RootOntology OWLOntology rootOntology) {
        super(accessManager, eventManager, applyChanges);
        this.renderingManager = renderingManager;
        this.rootOntology = rootOntology;
    }

    @Override
    public Class<DeleteEntityAction> getActionClass() {
        return DeleteEntityAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeDescriptionGenerator<OWLEntity> getChangeDescription(DeleteEntityAction action,
                                                                         ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<OWLEntity>(getChangeDescription(action.getSubject(), renderingManager.getBrowserText(action.getSubject())));
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(DeleteEntityAction action,
                                                                    ExecutionContext executionContext) {
        return new DeleteEntityChangeListGenerator(action.getSubject(), rootOntology);
    }

    @Override
    protected DeleteEntityResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult,
                                                    DeleteEntityAction action,
                                                    ExecutionContext executionContext,
                                                    EventList<ProjectEvent<?>> eventList) {
        return new DeleteEntityResult(eventList);
    }

    private String getChangeDescription(OWLEntity entity, String browserText) {
        return "Deleted " + getEntityTypeName(entity.getEntityType()).toLowerCase() + ": " + browserText;
    }


    private String getEntityTypeName(EntityType<?> entityType) {
        if(entityType == EntityType.CLASS) {
            return "Class";
        }
        else if(entityType == EntityType.OBJECT_PROPERTY) {
            return "Object property";
        }
        else if(entityType == EntityType.DATA_PROPERTY) {
            return "Data property";
        }
        else if(entityType == EntityType.ANNOTATION_PROPERTY) {
            return "Annotation property";
        }
        else if(entityType == EntityType.NAMED_INDIVIDUAL) {
            return "Named individual";
        }
        else {
            return "Datatype";
        }
    }

}
