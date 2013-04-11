package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityResult;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class DeleteEntityActionHandler extends AbstractProjectChangeHandler<OWLEntity, DeleteEntityAction, DeleteEntityResult> {

    private static final UserHasProjectWritePermissionValidator<DeleteEntityAction, DeleteEntityResult> VALIDATOR = new UserHasProjectWritePermissionValidator<DeleteEntityAction, DeleteEntityResult>();


    @Override
    public Class<DeleteEntityAction> getActionClass() {
        return DeleteEntityAction.class;
    }

    @Override
    protected RequestValidator<DeleteEntityAction> getAdditionalRequestValidator(DeleteEntityAction action, RequestContext requestContext) {
        return VALIDATOR;
    }

    @Override
    protected ChangeDescriptionGenerator<OWLEntity> getChangeDescription(DeleteEntityAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<OWLEntity>(getChangeDescription(action.getSubject(), project.getRenderingManager().getBrowserText(action.getSubject())));
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(DeleteEntityAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new DeleteEntityChangeListGenerator(action.getSubject());
    }

    @Override
    protected DeleteEntityResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult, DeleteEntityAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return new DeleteEntityResult(eventList);
    }

    private String getChangeDescription(OWLEntity entity, String browserText) {
        return "Deleted " + getEntityTypeName(entity.getEntityType()).toLowerCase() + ": " + browserText;
    }


    private String getEntityTypeName(EntityType entityType) {
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
