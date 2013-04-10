package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 * <p>
 *     An interface to an object that can handle a load project request.
 * </p>
 */
public interface LoadProjectRequestHandler {

    /**
     * Handle a request to load the project identified by the specified {@link ProjectId}.
     * @param projectId The {@link ProjectId} that identifies the project to be loaded.  Not {@code null}.
     */
    void handleProjectLoadRequest(ProjectId projectId);
}
