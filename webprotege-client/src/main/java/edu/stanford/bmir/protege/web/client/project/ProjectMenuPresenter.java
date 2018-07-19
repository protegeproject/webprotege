package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.tag.EditProjectTagsUIActionHandler;
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

    private final UploadAndMergeHandler uploadAndMergeHandler;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final EditProjectPrefixDeclarationsHandler editProjectPrefixDeclarationsHandler;

    private final EditProjectTagsUIActionHandler editProjectTagsUIActionHandler;

    private AbstractUiAction editProjectSettings = new AbstractUiAction(MESSAGES.projectSettings()) {
        @Override
        public void execute() {
            showProjectDetailsHandler.handleShowProjectDetails();
        }
    };

    private AbstractUiAction uploadAndMerge = new AbstractUiAction(MESSAGES.uploadAndMerge()) {
        @Override
        public void execute() {
            uploadAndMergeHandler.handleUploadAndMerge();
        }
    };

    private AbstractUiAction editProjectPrefixes = new AbstractUiAction(MESSAGES.prefixes_edit()) {
        @Override
        public void execute() {
            editProjectPrefixDeclarationsHandler.handleEditProjectPrefixes();
        }
    };

    private AbstractUiAction editProjectTags = new AbstractUiAction(MESSAGES.tags_EditProjectTags()) {
        @Override
        public void execute() {
            editProjectTagsUIActionHandler.handleEditProjectTags();
        }
    };

    @Inject
    public ProjectMenuPresenter(LoggedInUserProjectPermissionChecker permissionChecker,
                                ProjectMenuView view,
                                ShowProjectDetailsHandler showProjectDetailsHandler,
                                UploadAndMergeHandler uploadAndMergeHandler,
                                EditProjectPrefixDeclarationsHandler editProjectPrefixDeclarationsHandler,
                                EditProjectTagsUIActionHandler editProjectTagsUIActionHandler) {
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.showProjectDetailsHandler = showProjectDetailsHandler;
        this.uploadAndMergeHandler = uploadAndMergeHandler;
        this.editProjectPrefixDeclarationsHandler = editProjectPrefixDeclarationsHandler;
        this.editProjectTagsUIActionHandler = editProjectTagsUIActionHandler;
        setupActions();
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        editProjectSettings.setEnabled(false);
        uploadAndMerge.setEnabled(false);
        displayButton(container);
        permissionChecker.hasPermission(EDIT_PROJECT_SETTINGS,
                                        canEdit -> editProjectSettings.setEnabled(canEdit));
        permissionChecker.hasPermission(UPLOAD_AND_MERGE,
                                        canUploadAndMerge -> uploadAndMerge.setEnabled(canUploadAndMerge));
        permissionChecker.hasPermission(EDIT_PROJECT_PREFIXES,
                                        canEdit -> editProjectPrefixes.setEnabled(canEdit));
        permissionChecker.hasPermission(EDIT_PROJECT_TAGS,
                                        canEdit -> editProjectTags.setEnabled(canEdit));
    }

    public void dispose() {

    }

    private void displayButton(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void setupActions() {
        view.addMenuAction(editProjectSettings);
        view.addMenuAction(editProjectTags);
        view.addMenuAction(editProjectPrefixes);
        view.addMenuAction(uploadAndMerge);

    }


}
