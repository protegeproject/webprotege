package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.server.dispatch.validators.AdminPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsResult;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class GetProjectSharingSettingsActionHandler extends AbstractHasProjectActionHandler<GetProjectSharingSettingsAction, GetProjectSharingSettingsResult> {

    private final ProjectSharingSettingsManager sharingSettingsManager;

    private final ValidatorFactory<AdminPermissionValidator> validatorFactory;

    @Inject
    public GetProjectSharingSettingsActionHandler(OWLAPIProjectManager projectManager, ProjectSharingSettingsManager sharingSettingsManager, ValidatorFactory<AdminPermissionValidator> validatorFactory) {
        super(projectManager);
        this.sharingSettingsManager = sharingSettingsManager;
        this.validatorFactory = validatorFactory;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetProjectSharingSettingsAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected GetProjectSharingSettingsResult execute(GetProjectSharingSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ProjectSharingSettings settings = sharingSettingsManager.getProjectSharingSettings(action.getProjectId());
        return new GetProjectSharingSettingsResult(settings);
    }

    @Override
    public Class<GetProjectSharingSettingsAction> getActionClass() {
        return GetProjectSharingSettingsAction.class;
    }
}
