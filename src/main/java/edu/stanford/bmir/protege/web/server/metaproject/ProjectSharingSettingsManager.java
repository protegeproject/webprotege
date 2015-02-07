package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectSharingSettingsManager {

    void setProjectSharingSettings(ProjectSharingSettings projectSharingSettings);

    ProjectSharingSettings getProjectSharingSettings(ProjectId projectId);
}
