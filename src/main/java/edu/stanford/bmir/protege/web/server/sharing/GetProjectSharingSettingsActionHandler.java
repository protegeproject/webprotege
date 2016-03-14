package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
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

    private ProjectSharingSettingsManager sharingSettingsManager;

    @Inject
    public GetProjectSharingSettingsActionHandler(ProjectSharingSettingsManager sharingSettingsManager, OWLAPIProjectManager projectManager) {
        super(projectManager);
        this.sharingSettingsManager = checkNotNull(sharingSettingsManager);
    }

    @Override
    protected RequestValidator<GetProjectSharingSettingsAction> getAdditionalRequestValidator(GetProjectSharingSettingsAction action, RequestContext requestContext) {
        return NullValidator.get();
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
