package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class ProjectMenuPresenter implements HasDispose, Presenter {

    private static final Messages MESSAGES = GWT.create(Messages.class);

    private final ProjectMenuView view;

    private final ShowProjectDetailsHandler showProjectDetailsHandler;

    private final ShowFreshEntitySettingsHandler showFreshEntitySettingsHandler;

    private final UploadAndMergeHandler uploadAndMergeHandler;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private AbstractUiAction editProjectSettings = new AbstractUiAction(MESSAGES.settings()) {
        @Override
        public void execute(ClickEvent e) {
            showProjectDetailsHandler.handleShowProjectDetails();
        }
    };

    private AbstractUiAction editNewEntitySettings = new AbstractUiAction(MESSAGES.newEntitySettings()) {
        @Override
        public void execute(ClickEvent e) {
            showFreshEntitySettingsHandler.handleShowFreshEntitySettings();
        }
    };

    private AbstractUiAction uploadAndMerge = new AbstractUiAction(MESSAGES.uploadAndMerge()) {
        @Override
        public void execute(ClickEvent e) {
            uploadAndMergeHandler.handleUploadAndMerge();
        }
    };

    @Inject
    public ProjectMenuPresenter(LoggedInUserProjectPermissionChecker permissionChecker,
                                ProjectMenuView view,
                                ShowProjectDetailsHandler showProjectDetailsHandler,
                                ShowFreshEntitySettingsHandler showFreshEntitySettingsHandler,
                                UploadAndMergeHandler uploadAndMergeHandler) {
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.showProjectDetailsHandler = showProjectDetailsHandler;
        this.showFreshEntitySettingsHandler = showFreshEntitySettingsHandler;
        this.uploadAndMergeHandler = uploadAndMergeHandler;
        setupActions();
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        editProjectSettings.setEnabled(false);
        editNewEntitySettings.setEnabled(false);
        uploadAndMerge.setEnabled(false);
        displayButton(container);
        permissionChecker.hasPermission(EDIT_PROJECT_SETTINGS,
                                        canEdit -> editProjectSettings.setEnabled(canEdit));
        permissionChecker.hasPermission(UPLOAD_AND_MERGE,
                                        canUploadAndMerge -> uploadAndMerge.setEnabled(canUploadAndMerge));
        permissionChecker.hasPermission(EDIT_NEW_ENTITY_SETTINGS,
                                        canEdit -> editNewEntitySettings.setEnabled(canEdit));
    }

    public void dispose() {

    }

    private void displayButton(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void setupActions() {
        view.addMenuAction(editProjectSettings);
        view.addMenuAction(editNewEntitySettings);
        view.addMenuAction(uploadAndMerge);
    }


}
