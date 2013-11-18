package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateDataPropertiesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateDataPropertiesResult;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLDataProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateDataPropertiesActionHandler extends AbstractProjectChangeHandler<Set<OWLDataProperty>, CreateDataPropertiesAction, CreateDataPropertiesResult> {

    @Override
    protected ChangeListGenerator<Set<OWLDataProperty>> getChangeListGenerator(CreateDataPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new CreateDataPropertiesChangeGenerator(action.getBrowserTexts(), action.getParent());
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLDataProperty>> getChangeDescription(CreateDataPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<Set<OWLDataProperty>>(action.getBrowserTexts().size() == 1 ? "Created data property" : "Created data properties");
    }

    @Override
    protected CreateDataPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLDataProperty>> changeApplicationResult, CreateDataPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        Map<OWLDataProperty, String> map = new HashMap<OWLDataProperty, String>();
        for(OWLDataProperty dataProperty : changeApplicationResult.getSubject().get()) {
            map.put(dataProperty, project.getRenderingManager().getBrowserText(dataProperty));
        }
        return new CreateDataPropertiesResult(map, project.getProjectId(), action.getParent(), eventList);
    }

    @Override
    protected RequestValidator<CreateDataPropertiesAction> getAdditionalRequestValidator(CreateDataPropertiesAction action, RequestContext requestContext) {
        return new UserHasProjectWritePermissionValidator<CreateDataPropertiesAction>();
    }

    @Override
    public Class<CreateDataPropertiesAction> getActionClass() {
        return CreateDataPropertiesAction.class;
    }
}
