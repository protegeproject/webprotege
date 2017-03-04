package edu.stanford.bmir.protege.web.client.projectmanager;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 */
public interface DownloadProjectRequestHandler {

    /**
     * Handle a request to download the specified project.  The project is identified by its {@link ProjectId}.
     * @param projectId The {@link ProjectId} that identifies the project to be downloaded.
     */
    void handleProjectDownloadRequest(ProjectId projectId);
}
