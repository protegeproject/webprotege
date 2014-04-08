package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateAnnotationPropertiesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateAnnotationPropertiesResult;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateAnnotationPropertiesActionHandler extends AbstractProjectChangeHandler<Set<OWLAnnotationProperty>, CreateAnnotationPropertiesAction, CreateAnnotationPropertiesResult> {

    @Override
    protected ChangeListGenerator<Set<OWLAnnotationProperty>> getChangeListGenerator(CreateAnnotationPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new CreateAnnotationPropertiesChangeGenerator(action.getBrowserTexts(), action.getParent());
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLAnnotationProperty>> getChangeDescription(CreateAnnotationPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<Set<OWLAnnotationProperty>>("Created annotation properties");
    }

    @Override
    protected CreateAnnotationPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLAnnotationProperty>> changeApplicationResult, CreateAnnotationPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        Map<OWLAnnotationProperty, String> map = new HashMap<OWLAnnotationProperty, String>();
        for(OWLAnnotationProperty result : changeApplicationResult.getSubject().get()) {
            map.put(result, project.getRenderingManager().getBrowserText(result));
        }
        return new CreateAnnotationPropertiesResult(map, project.getProjectId(), action.getParent(), eventList);
    }

    @Override
    protected RequestValidator<CreateAnnotationPropertiesAction> getAdditionalRequestValidator(CreateAnnotationPropertiesAction action, RequestContext requestContext) {
        return new UserHasProjectWritePermissionValidator<CreateAnnotationPropertiesAction>();
    }

    @Override
    public Class<CreateAnnotationPropertiesAction> getActionClass() {
        return CreateAnnotationPropertiesAction.class;
    }
}
