package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 */
public interface TrashManagerRequestHandler {

    void handleMoveProjectToTrash(ProjectId projectId);

    void handleRemoveProjectFromTrash(ProjectId projectId);
}
