package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.EmptySuccessWebProtegeCallback;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.project.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class TrashManagerRequestHandlerImpl implements TrashManagerRequestHandler {

    private static final ProjectManagerServiceAsync PROJECT_MANAGER_SERVICE_ASYNC = GWT.create(ProjectManagerService.class);

    @Override
    public void handleMoveProjectToTrash(final ProjectId projectId) {
        DispatchServiceManager.get().execute(new MoveProjectsToTrashAction(Collections.singleton(projectId)), new EmptySuccessWebProtegeCallback<MoveProjectsToTrashResult>());
    }

    @Override
    public void handleRemoveProjectFromTrash(final ProjectId projectId) {
        DispatchServiceManager.get().execute(new RemoveProjectsFromTrashAction(Collections.singleton(projectId)), new EmptySuccessWebProtegeCallback<RemoveProjectsFromTrashResult>());
    }
}
