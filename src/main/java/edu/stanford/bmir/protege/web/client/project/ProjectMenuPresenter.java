package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.actionbar.project.ShowFreshEntitySettingsHandler;
import edu.stanford.bmir.protege.web.client.actionbar.project.ShowNotificationSettingsHandler;
import edu.stanford.bmir.protege.web.client.actionbar.project.ShowProjectDetailsHandler;
import edu.stanford.bmir.protege.web.client.actionbar.project.UploadAndMergeHandler;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerView;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserPresenter;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class ProjectMenuPresenter {

    private final ProjectMenuView view;

    private final ShowProjectDetailsHandler showProjectDetailsHandler;

    private final ShowFreshEntitySettingsHandler showFreshEntitySettingsHandler;

    private final UploadAndMergeHandler uploadAndMergeHandler;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public ProjectMenuPresenter(ProjectMenuView view, LoggedInUserProvider loggedInUserProvider, ShowProjectDetailsHandler showProjectDetailsHandler, ShowFreshEntitySettingsHandler showFreshEntitySettingsHandler, UploadAndMergeHandler uploadAndMergeHandler) {
        this.view = view;
        this.loggedInUserProvider = loggedInUserProvider;
        this.showProjectDetailsHandler = showProjectDetailsHandler;
        this.showFreshEntitySettingsHandler = showFreshEntitySettingsHandler;
        this.uploadAndMergeHandler = uploadAndMergeHandler;
        setupActions();
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void setupActions() {

        view.addMenuAction(new AbstractUiAction("Settings") {
            @Override
            public void execute() {
                showProjectDetailsHandler.handleShowProjectDetails();
            }
        });
        view.addMenuAction(new AbstractUiAction("New entity settings") {
            @Override
            public void execute() {
                showFreshEntitySettingsHandler.handleShowFreshEntitySettings();
            }
        });
        view.addMenuAction(new AbstractUiAction("Upload and merge") {
            @Override
            public void execute() {
                uploadAndMergeHandler.handleUploadAndMerge();
            }
        });
    }


}
