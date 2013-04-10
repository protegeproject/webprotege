package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

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
        Set<ProjectId> toTrash = new HashSet<ProjectId>(1);
        toTrash.add(projectId);
        PROJECT_MANAGER_SERVICE_ASYNC.moveProjectsToTrash(toTrash, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert("Could not move selected ontologies to trash.  Reason: " + caught.getMessage());
                GWT.log("Probkem moving ontologies to trash: ", caught);
            }

            public void onSuccess(Void result) {
                EventBusManager.getManager().postEvent(new ProjectMovedToTrashEvent(projectId));
            }
        });
    }

    @Override
    public void handleRemoveProjectFromTrash(final ProjectId projectId) {
        Set<ProjectId> fromTrash = new HashSet<ProjectId>(1);
        fromTrash.add(projectId);

        PROJECT_MANAGER_SERVICE_ASYNC.removeProjectsFromTrash(fromTrash, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO:
            }

            @Override
            public void onSuccess(Void result) {
                EventBusManager.getManager().postEvent(new ProjectMovedToTrashEvent(projectId));
            }
        });
    }
}
