package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.NewProjectDialogController;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class CreateProjectRequestHandlerImpl implements CreateProjectRequestHandler {

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public CreateProjectRequestHandlerImpl(EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @Override
    public void handleCreateProjectRequest() {
        // Code splitting
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                WebProtegeDialog.showDialog(new NewProjectDialogController(eventBus, dispatchServiceManager, loggedInUserProvider));
            }
        });
    }
}
