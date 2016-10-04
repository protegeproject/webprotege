package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateObjectPropertiesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateObjectPropertiesResult;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.dispatch.validators.WritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertyActionHandler extends AbstractProjectChangeHandler<Set<OWLObjectProperty>, CreateObjectPropertiesAction, CreateObjectPropertiesResult> {

    private final ValidatorFactory<WritePermissionValidator> validatorFactory;

    @Inject
    public CreateObjectPropertyActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<WritePermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Class<CreateObjectPropertiesAction> getActionClass() {
        return CreateObjectPropertiesAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(CreateObjectPropertiesAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected ChangeListGenerator<Set<OWLObjectProperty>> getChangeListGenerator(CreateObjectPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new CreateObjectPropertiesChangeGenerator(action.getBrowserTexts(), action.getParent());
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLObjectProperty>> getChangeDescription(CreateObjectPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<Set<OWLObjectProperty>>("Created object properties");
    }

    @Override
    protected CreateObjectPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLObjectProperty>> changeApplicationResult, CreateObjectPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        Optional<Set<OWLObjectProperty>> result = changeApplicationResult.getSubject();
        Map<OWLObjectProperty, String> map = new HashMap<OWLObjectProperty, String>();
        for(OWLObjectProperty prop : result.get()) {
            map.put(prop, project.getRenderingManager().getBrowserText(prop));
        }
        return new CreateObjectPropertiesResult(map, project.getProjectId(), action.getParent(), eventList);
    }
}
