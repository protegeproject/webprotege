package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsProjectOwnerValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;

import javax.inject.Inject;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class SetProjectSharingSettingsActionHandler extends AbstractHasProjectActionHandler<SetProjectSharingSettingsAction, SetProjectSharingSettingsResult> {

    private final ProjectSharingSettingsManager sharingSettingsManager;

    private final ValidatorFactory<UserIsProjectOwnerValidator> validator;

    @Inject
    public SetProjectSharingSettingsActionHandler(OWLAPIProjectManager projectManager, ProjectSharingSettingsManager sharingSettingsManager, ValidatorFactory<UserIsProjectOwnerValidator> validator) {
        super(projectManager);
        this.sharingSettingsManager = sharingSettingsManager;
        this.validator = validator;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(SetProjectSharingSettingsAction action, RequestContext requestContext) {
        return validator.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected SetProjectSharingSettingsResult execute(SetProjectSharingSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        sharingSettingsManager.setProjectSharingSettings(action.getProjectSharingSettings());
        return new SetProjectSharingSettingsResult();
    }

    @Override
    public Class<SetProjectSharingSettingsAction> getActionClass() {
        return SetProjectSharingSettingsAction.class;
    }
}
