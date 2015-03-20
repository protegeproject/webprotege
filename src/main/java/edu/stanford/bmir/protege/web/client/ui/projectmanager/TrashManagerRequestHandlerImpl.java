package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.EmptySuccessWebProtegeCallback;
import edu.stanford.bmir.protege.web.shared.project.*;

import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class TrashManagerRequestHandlerImpl implements TrashManagerRequestHandler {

    @Override
    public void handleMoveProjectToTrash(final ProjectId projectId) {
        DispatchServiceManager.get().execute(new MoveProjectsToTrashAction(Collections.singleton(projectId)), new EmptySuccessWebProtegeCallback<MoveProjectsToTrashResult>());
    }

    @Override
    public void handleRemoveProjectFromTrash(final ProjectId projectId) {
        DispatchServiceManager.get().execute(new RemoveProjectsFromTrashAction(Collections.singleton(projectId)), new EmptySuccessWebProtegeCallback<RemoveProjectsFromTrashResult>());
    }
}
