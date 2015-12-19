package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.UploadProjectDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class UploadProjectRequestHandlerImpl implements UploadProjectRequestHandler {

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    public UploadProjectRequestHandlerImpl(EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void handleUploadProjectRequest() {
        // Code splitting
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                WebProtegeDialog.showDialog(new UploadProjectDialogController(eventBus, dispatchServiceManager));
            }
        });
    }
}
