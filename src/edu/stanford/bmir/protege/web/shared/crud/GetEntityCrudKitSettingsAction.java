package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitSettingsAction implements HasProjectAction<GetEntityCrudKitSettingsResult> {

    private ProjectId projectId;

    private GetEntityCrudKitSettingsAction() {
    }

    public GetEntityCrudKitSettingsAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
