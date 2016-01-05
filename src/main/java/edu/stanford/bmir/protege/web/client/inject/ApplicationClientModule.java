package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.HasClientApplicationProperties;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.actionbar.application.*;
import edu.stanford.bmir.protege.web.client.actionbar.project.*;
import edu.stanford.bmir.protege.web.client.banner.BannerView;
import edu.stanford.bmir.protege.web.client.banner.BannerViewImpl;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionCheckerImpl;
import edu.stanford.bmir.protege.web.client.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.ProjectPermissionManager;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.place.PlaceManager;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManagerImpl;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.ui.editor.EditorManagerSelector;
import edu.stanford.bmir.protege.web.client.ui.editor.EntityDataContextSelector;
import edu.stanford.bmir.protege.web.client.ui.frame.ManchesterSyntaxFrameEditor;
import edu.stanford.bmir.protege.web.client.ui.frame.ManchesterSyntaxFrameEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.notes.*;
import edu.stanford.bmir.protege.web.client.ui.obo.OBOTermCrossProductEditor;
import edu.stanford.bmir.protege.web.client.ui.obo.OBOTermCrossProductEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.ontology.annotations.AnnotationsView;
import edu.stanford.bmir.protege.web.client.ui.ontology.annotations.AnnotationsViewImpl;
import edu.stanford.bmir.protege.web.client.ui.projectlist.ProjectListView;
import edu.stanford.bmir.protege.web.client.ui.projectlist.ProjectListViewImpl;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceView;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceViewImpl;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationPropertiesProvider;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
public class ApplicationClientModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(ProjectId.class).toProvider(ProjectIdProvider.class);
        bind(Project.class).toProvider(ProjectProvider.class);
        bind(ActiveProjectManager.class).to(ActiveProjectManagerImpl.class).asEagerSingleton();
        bind(PermissionChecker.class).to(ProjectPermissionManager.class);

        bind(HasClientApplicationProperties.class).to(ClientApplicationProperties.class);
        bind(ClientApplicationProperties.class).toProvider(ClientApplicationPropertiesProvider.class).asEagerSingleton();


        bind(EventBus.class).to(SimpleEventBus.class).asEagerSingleton();
        bind(PlaceController.class).toProvider(PlaceControllerProvider.class).asEagerSingleton();
        bind(ProjectManager.class).asEagerSingleton();
        bind(DispatchServiceManager.class).asEagerSingleton();
        bind(WorkspaceView.class).to(WorkspaceViewImpl.class);

        bind(LoggedInUserProvider.class).to(LoggedInUserManager.class).asEagerSingleton();
        bind(LoggedInUserManager.class).toProvider(LoggedInUserManagerProvider.class).asEagerSingleton();
        bind(PlaceManager.class).asEagerSingleton();
        bind(SelectionModel.class).asEagerSingleton();

        bind(OBOTermCrossProductEditor.class).to(OBOTermCrossProductEditorImpl.class);
        bind(AnnotationsView.class).to(AnnotationsViewImpl.class);
        bind(DiscussionThreadView.class).to(DiscussionThreadViewImpl.class);
        bind(NoteContainerView.class).to(NoteContainerViewImpl.class);
        bind(NoteHeaderView.class).to(NoteHeaderViewImpl.class);
        bind(NoteView.class).to(NoteViewImpl.class);
        bind(NoteActionView.class).to(NoteActionViewImpl.class);
        bind(ReplyToNoteHandler.class).to(ReplyToNoteHandlerImpl.class);
        bind(DeleteNoteHandler.class).to(DeleteNoteHandlerImpl.class);

        bind(ManchesterSyntaxFrameEditor.class).to(ManchesterSyntaxFrameEditorImpl.class);

        bind(ShowShareSettingsHandler.class).to(ShareSettingsHandlerImpl.class);
        bind(ShowProjectDetailsHandler.class).to(ShowProjectDetailsHandlerImpl.class);
        bind(UploadAndMergeHandler.class).to(UploadAndMergeHandlerImpl.class);
        bind(SignInRequestHandler.class).to(SignInRequestHandlerImpl.class);
        bind(SignOutRequestHandler.class).to(SignOutRequestHandlerImpl.class);
        bind(SignUpForAccountHandler.class).to(SignUpForAccountHandlerImpl.class);
        bind(ChangeEmailAddressHandler.class).to(ChangeEmailAddressHandlerImpl.class);
        bind(ShowUserGuideHandler.class).to(ShowUserGuideHandlerImpl.class);

        bind(ShowFreshEntitySettingsHandler.class).to(ShowFreshEntitySettingsHandlerImpl.class);
        bind(ChangePasswordHandler.class).to(ChangePasswordHandlerImpl.class);
        bind(ShowAboutBoxHandler.class).to(ShowAboutBoxHandlerImpl.class);

        bind(BannerView.class).to(BannerViewImpl.class);
        bind(ApplicationActionBar.class).to(ApplicationActionBarImpl.class);
        bind(ProjectActionBar.class).to(ProjectActionBarImpl.class);
        bind(ProjectListView.class).to(ProjectListViewImpl.class);

        bind(LoggedInUserProjectPermissionChecker.class).to(LoggedInUserProjectPermissionCheckerImpl.class);

        bind(EditorManagerSelector.class).to(EntityDataContextSelector.class);
    }

}
