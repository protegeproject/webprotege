package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.HasClientApplicationProperties;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.actionbar.application.*;
import edu.stanford.bmir.protege.web.client.actionbar.project.*;
import edu.stanford.bmir.protege.web.client.banner.BannerView;
import edu.stanford.bmir.protege.web.client.banner.BannerViewImpl;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewImpl;
import edu.stanford.bmir.protege.web.client.chgpwd.ResetPasswordView;
import edu.stanford.bmir.protege.web.client.chgpwd.ResetPasswordViewImpl;
import edu.stanford.bmir.protege.web.client.help.*;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListView;
import edu.stanford.bmir.protege.web.client.login.LoginPresenter;
import edu.stanford.bmir.protege.web.client.login.LoginView;
import edu.stanford.bmir.protege.web.client.login.LoginViewImpl;
import edu.stanford.bmir.protege.web.client.logout.LogoutView;
import edu.stanford.bmir.protege.web.client.logout.LogoutViewImpl;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionCheckerImpl;
import edu.stanford.bmir.protege.web.client.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.PermissionManager;
import edu.stanford.bmir.protege.web.client.perspective.*;
import edu.stanford.bmir.protege.web.client.place.*;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManagerImpl;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenterFactory;
import edu.stanford.bmir.protege.web.client.project.ProjectView;
import edu.stanford.bmir.protege.web.client.project.ProjectViewImpl;
import edu.stanford.bmir.protege.web.client.signup.SignUpView;
import edu.stanford.bmir.protege.web.client.signup.SignUpViewImpl;
import edu.stanford.bmir.protege.web.client.topbar.GoToHomeView;
import edu.stanford.bmir.protege.web.client.topbar.GoToToHomeViewImpl;
import edu.stanford.bmir.protege.web.client.topbar.TopBarView;
import edu.stanford.bmir.protege.web.client.topbar.TopBarViewImpl;
import edu.stanford.bmir.protege.web.client.ui.CreateFreshPerspectiveRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.editor.EditorManagerSelector;
import edu.stanford.bmir.protege.web.client.ui.editor.EntityDataContextSelector;
import edu.stanford.bmir.protege.web.client.ui.frame.ManchesterSyntaxFrameEditor;
import edu.stanford.bmir.protege.web.client.ui.frame.ManchesterSyntaxFrameEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.individuals.IndividualsListViewImpl;
import edu.stanford.bmir.protege.web.client.ui.notes.*;
import edu.stanford.bmir.protege.web.client.ui.obo.OBOTermCrossProductEditor;
import edu.stanford.bmir.protege.web.client.ui.obo.OBOTermCrossProductEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.ontology.annotations.AnnotationsView;
import edu.stanford.bmir.protege.web.client.ui.ontology.annotations.AnnotationsViewImpl;
import edu.stanford.bmir.protege.web.client.ui.projectlist.*;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.*;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveFactory;
import edu.stanford.bmir.protege.web.client.user.*;
import edu.stanford.bmir.protege.web.client.workspace.ApplicationPresenter;
import edu.stanford.bmir.protege.web.client.workspace.ApplicationView;
import edu.stanford.bmir.protege.web.client.workspace.ApplicationViewImpl;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationPropertiesProvider;
import edu.stanford.bmir.protege.web.shared.auth.Md5MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
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

        install(new GinFactoryModuleBuilder()
                .implement(Perspective.class, PerspectiveImpl.class)
                .build(PerspectiveFactory.class));

        bind(ProjectId.class).toProvider(ProjectIdProvider.class);
        bind(Project.class).toProvider(ProjectProvider.class);
        bind(ActiveProjectManager.class).to(ActiveProjectManagerImpl.class).asEagerSingleton();
        bind(PermissionManager.class).asEagerSingleton();
        bind(PermissionChecker.class).to(PermissionManager.class);

        bind(HasClientApplicationProperties.class).to(ClientApplicationProperties.class);
        bind(ClientApplicationProperties.class).toProvider(ClientApplicationPropertiesProvider.class).asEagerSingleton();


        bind(EventBus.class).to(SimpleEventBus.class).asEagerSingleton();
        bind(PlaceController.class).toProvider(PlaceControllerProvider.class).asEagerSingleton();
        bind(ProjectManager.class).asEagerSingleton();
        bind(DispatchServiceManager.class).asEagerSingleton();
        bind(ApplicationView.class).to(ApplicationViewImpl.class);

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

        bind(SignUpView.class).to(SignUpViewImpl.class);

        bind(ChangeEmailAddressHandler.class).to(ChangeEmailAddressHandlerImpl.class);
        bind(ShowUserGuideHandler.class).to(ShowUserGuideHandlerImpl.class);

        bind(ShowFreshEntitySettingsHandler.class).to(ShowFreshEntitySettingsHandlerImpl.class);
        bind(ChangePasswordHandler.class).to(ChangePasswordHandlerImpl.class);
        bind(ShowAboutBoxHandler.class).to(ShowAboutBoxHandlerImpl.class);

        bind(BannerView.class).to(BannerViewImpl.class);
        bind(ApplicationActionBar.class).to(ApplicationActionBarImpl.class);
        bind(ProjectActionBar.class).to(ProjectActionBarImpl.class);
//        bind(ProjectListView.class).to(ProjectListViewImpl.class);

        bind(LoggedInUserProjectPermissionChecker.class).to(LoggedInUserProjectPermissionCheckerImpl.class);


        bind(EditorManagerSelector.class).to(EntityDataContextSelector.class);

        bind(ChangeListView.class).to(ChangeListViewImpl.class);

        bind(ProjectView.class).to(ProjectViewImpl.class);
        install(new GinFactoryModuleBuilder()
                .implement(ProjectPresenter.class, ProjectPresenter.class)
                .build(ProjectPresenterFactory.class));

        bind(TopBarView.class).to(TopBarViewImpl.class);

        bind(ApplicationPresenter.class).asEagerSingleton();
        bind(LoginPresenter.class).asEagerSingleton();
        bind(ProjectManagerPresenter.class).asEagerSingleton();
        bind(ProjectManagerView.class).to(ProjectManagerViewImpl.class);
        bind(ProjectDetailsView.class).to(ProjectDetailsViewImpl.class);
        bind(LoadProjectRequestHandler.class).to(LoadProjectRequestHandlerImpl.class);
        bind(LoadProjectInNewWindowRequestHandler.class).to(LoadProjectInNewWindowRequestHandlerImpl.class);
        bind(DownloadProjectRequestHandler.class).to(DownloadProjectRequestHandlerImpl.class);
        bind(TrashManagerRequestHandler.class).to(TrashManagerRequestHandlerImpl.class);


        bind(LoginView.class).to(LoginViewImpl.class);
        bind(LogoutView.class).to(LogoutViewImpl.class);
        bind(ResetPasswordView.class).to(ResetPasswordViewImpl.class);
        bind(LoggedInUserView.class).to(LoggedInUserViewImpl.class);

        bind(HelpView.class).to(HelpViewImpl.class);

        bind(GoToHomeView.class).to(GoToToHomeViewImpl.class);

        bind(MessageDigestAlgorithm.class).to(Md5MessageDigestAlgorithm.class);

        bind(PlaceHistoryHandler.class).toProvider(PlaceHistoryHandlerProvider.class).asEagerSingleton();
        bind(ActivityMapper.class).to(WebProtegeActivityMapper.class).asEagerSingleton();
        bind(ActivityManager.class).to(WebProtegeActivityManager.class).asEagerSingleton();

        bind(PerspectiveSwitcherView.class).to(PerspectiveSwitcherViewImpl.class);
        bind(PerspectiveView.class).to(PerspectiveViewImpl.class);
        bind(PerspectiveLinkManager.class).to(PerspectiveLinkManagerImpl.class);
        bind(CreateFreshPerspectiveRequestHandler.class).to(CreateFreshPerspectiveRequestHandlerImpl.class);

        install(new GinFactoryModuleBuilder()
                .implement(PerspectiveLink.class, PerspectiveLinkImpl.class)
                .build(PerspectiveLinkFactory.class));

        bind(IndividualsListView.class).to(IndividualsListViewImpl.class);

        install(new GinFactoryModuleBuilder()
                .implement(ProjectDetailsPresenter.class, ProjectDetailsPresenter.class)
                .build(ProjectDetailPresenterFactory.class));

    }

}
