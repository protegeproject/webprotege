package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class ProjectMenuPresenter {

    private final ProjectId projectId;

    private final DispatchServiceManager dispatchServiceManager;

    private final ProjectMenuView view;

    private final ShowProjectDetailsHandler showProjectDetailsHandler;

    private final ShowFreshEntitySettingsHandler showFreshEntitySettingsHandler;

    private final UploadAndMergeHandler uploadAndMergeHandler;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public ProjectMenuPresenter(ProjectId projectId, DispatchServiceManager dispatchServiceManager, ProjectMenuView view, ShowProjectDetailsHandler showProjectDetailsHandler, ShowFreshEntitySettingsHandler showFreshEntitySettingsHandler, UploadAndMergeHandler uploadAndMergeHandler, LoggedInUserProvider loggedInUserProvider) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.view = view;
        this.showProjectDetailsHandler = showProjectDetailsHandler;
        this.showFreshEntitySettingsHandler = showFreshEntitySettingsHandler;
        this.uploadAndMergeHandler = uploadAndMergeHandler;
        this.loggedInUserProvider = loggedInUserProvider;
        setupActions();
    }

    public void start(final AcceptsOneWidget container) {
        dispatchServiceManager.execute(new GetPermissionsAction(projectId, loggedInUserProvider.getCurrentUserId()), new DispatchServiceCallback<GetPermissionsResult>() {
            @Override
            public void handleSuccess(GetPermissionsResult result) {
                if(result.getPermissionsSet().contains(Permission.getAdminPermission())) {
                    displayButton(container);
                }
                else {
                    view.asWidget().removeFromParent();
                }
            }
        });
    }

    private void displayButton(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void setupActions() {

        view.addMenuAction(new AbstractUiAction("Settings") {
            @Override
            public void execute(ClickEvent e) {
                showProjectDetailsHandler.handleShowProjectDetails();
            }
        });
        view.addMenuAction(new AbstractUiAction("New entity settings") {
            @Override
            public void execute(ClickEvent e) {
                showFreshEntitySettingsHandler.handleShowFreshEntitySettings();
            }
        });
        view.addMenuAction(new AbstractUiAction("Upload and merge") {
            @Override
            public void execute(ClickEvent e) {
                uploadAndMergeHandler.handleUploadAndMerge();
            }
        });
    }


}
