package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectSharingSettingsManager {

    void setProjectSharingSettings(ProjectSharingSettings projectSharingSettings);

    ProjectSharingSettings getProjectSharingSettings(ProjectId projectId);
}
