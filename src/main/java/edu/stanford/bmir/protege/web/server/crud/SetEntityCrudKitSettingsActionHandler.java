package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.FindAndReplaceIRIPrefixChangeGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.AdminPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.crud.IRIPrefixUpdateStrategy;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsResult;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SetEntityCrudKitSettingsActionHandler extends AbstractHasProjectActionHandler<SetEntityCrudKitSettingsAction, SetEntityCrudKitSettingsResult> {

    private final ValidatorFactory<AdminPermissionValidator> validatorFactory;

    @Inject
    public SetEntityCrudKitSettingsActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<AdminPermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Class<SetEntityCrudKitSettingsAction> getActionClass() {
        return SetEntityCrudKitSettingsAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(SetEntityCrudKitSettingsAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected SetEntityCrudKitSettingsResult execute(SetEntityCrudKitSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        project.setEntityCrudKitSettings(action.getToSettings());
        if(action.getPrefixUpdateStrategy() == IRIPrefixUpdateStrategy.FIND_AND_REPLACE) {
            String fromPrefix = action.getFromSettings().getPrefixSettings().getIRIPrefix();
            String toPrefix = action.getToSettings().getPrefixSettings().getIRIPrefix();
            FindAndReplaceIRIPrefixChangeGenerator changeGenerator = new FindAndReplaceIRIPrefixChangeGenerator(fromPrefix, toPrefix);
            project.applyChanges(executionContext.getUserId(), changeGenerator, new FixedMessageChangeDescriptionGenerator<Void>("Replaced IRI prefix <" + fromPrefix + "> with <" + toPrefix + ">"));
        }
        return new SetEntityCrudKitSettingsResult();
    }

}
