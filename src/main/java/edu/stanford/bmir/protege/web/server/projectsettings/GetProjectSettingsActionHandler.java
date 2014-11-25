package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<GetProjectSettingsAction, GetProjectSettingsResult> {

    private OWLAPIProjectMetadataManager mdm;

    public GetProjectSettingsActionHandler(OWLAPIProjectMetadataManager mdm) {
        this.mdm = checkNotNull(mdm);
    }

    @Override
    public Class<GetProjectSettingsAction> getActionClass() {
        return GetProjectSettingsAction.class;
    }

    @Override
    protected RequestValidator<GetProjectSettingsAction> getAdditionalRequestValidator(GetProjectSettingsAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetProjectSettingsResult execute(GetProjectSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        OWLAPIProjectType projectType = mdm.getType(projectId);
        ProjectType type = new ProjectType(projectType.getProjectTypeName());
        ProjectSettings projectSettings = new ProjectSettings(
                projectId,
                type,
                mdm.getDescription(projectId)
        );
        return new GetProjectSettingsResult(projectSettings);
    }

}
