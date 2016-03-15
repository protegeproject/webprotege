package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.EmptySuccessWebProtegeCallback;
import edu.stanford.bmir.protege.web.shared.project.*;

import javax.inject.Inject;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class TrashManagerRequestHandlerImpl implements TrashManagerRequestHandler {

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public TrashManagerRequestHandlerImpl(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void handleMoveProjectToTrash(final ProjectId projectId) {
        dispatchServiceManager.execute(new MoveProjectsToTrashAction(projectId), new EmptySuccessWebProtegeCallback<MoveProjectsToTrashResult>());
    }

    @Override
    public void handleRemoveProjectFromTrash(final ProjectId projectId) {
        dispatchServiceManager.execute(new RemoveProjectFromTrashAction(projectId), new EmptySuccessWebProtegeCallback<RemoveProjectsFromTrashResult>());
    }
}
