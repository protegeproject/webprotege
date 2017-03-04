package edu.stanford.bmir.protege.web.client.project;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.user.NotSignedInException;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.projectmanager.ProjectCreatedEvent;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectAction;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyRegisteredException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class NewProjectDialogController extends WebProtegeOKCancelDialogController<NewProjectInfo> {

    public static final String TITLE = "Create project";

    private final DispatchServiceManager dispatchServiceManager;

    private NewProjectInfoWidget widget = new NewProjectInfoWidget();

    private final EventBus eventBus;

    private final LoggedInUserProvider loggedInUserProvider;

    public NewProjectDialogController(EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        super(TITLE);
        this.eventBus = eventBus;
        this.loggedInUserProvider = loggedInUserProvider;
        this.dispatchServiceManager = dispatchServiceManager;
        this.widget = new NewProjectInfoWidget();
        for(WebProtegeDialogValidator validator : widget.getDialogValidators()) {
            addDialogValidator(validator);
        }
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<NewProjectInfo>() {
            public void handleHide(NewProjectInfo data, WebProtegeDialogCloser closer) {
                handleCreateNewProject(data);
                closer.hide();
            }
        });
    }

    private void handleCreateNewProject(NewProjectInfo data) {
        UserId userId = loggedInUserProvider.getCurrentUserId();
        if(userId.isGuest()) {
            throw new RuntimeException("User is guest.  Guest users are not allowed to create projects.");
        }
        NewProjectSettings newProjectSettings = new NewProjectSettings(userId, data.getProjectName(), data.getProjectDescription());
        dispatchServiceManager.execute(new CreateNewProjectAction(newProjectSettings), new DispatchServiceCallbackWithProgressDisplay<CreateNewProjectResult>() {
            @Override
            public String getProgressDisplayTitle() {
                return "Creating project";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Please wait.";
            }

            @Override
            public void handleSuccess(CreateNewProjectResult result) {
                eventBus.fireEvent(new ProjectCreatedEvent(result.getProjectDetails()));
            }

            @Override
            public void handleExecutionException(Throwable cause) {
                if (cause instanceof NotSignedInException) {
                    MessageBox.showMessage("You must be signed in to create new projects");
                }
                else if (cause instanceof ProjectAlreadyRegisteredException) {
                    ProjectAlreadyRegisteredException ex = (ProjectAlreadyRegisteredException) cause;
                    String projectName = ex.getProjectId().getId();
                    MessageBox.showMessage("The project name " + projectName + " is already registered.  Please try a different name.");
                }
                else if (cause instanceof ProjectDocumentExistsException) {
                    ProjectDocumentExistsException ex = (ProjectDocumentExistsException) cause;
                    String projectName = ex.getProjectId().getId();
                    MessageBox.showMessage("There is already a non-empty project on the server with the id " + projectName + ".  This project has NOT been overwritten.  Please contact the administrator to resolve this issue.");
                }
                else {
                    MessageBox.showMessage(cause.getMessage());
                }
            }
        });
    }


    public Widget getWidget() {
        return widget;
    }

    public Optional<Focusable> getInitialFocusable() {
        return widget.getInitialFocusable();
    }

    public NewProjectInfo getData() {
        return widget.getNewProjectInfo();
    }
}
