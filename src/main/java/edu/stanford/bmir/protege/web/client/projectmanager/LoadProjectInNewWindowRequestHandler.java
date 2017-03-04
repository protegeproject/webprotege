package edu.stanford.bmir.protege.web.client.projectmanager;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
public interface LoadProjectInNewWindowRequestHandler {

    void handleLoadProjectInNewWindow(ProjectId projectId);
}
