package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.web.bindery.event.shared.EventBus;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.client.app.HasClientApplicationProperties;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.portlet.PortletUiImpl;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.app.ForbiddenViewImpl;
import edu.stanford.bmir.protege.web.client.chgpwd.ResetPasswordView;
import edu.stanford.bmir.protege.web.client.chgpwd.ResetPasswordViewImpl;
import edu.stanford.bmir.protege.web.client.dispatch.SignInRequiredHandler;
import edu.stanford.bmir.protege.web.client.dispatch.SignInRequiredHandlerImpl;
import edu.stanford.bmir.protege.web.client.events.EventPollingPeriod;
import edu.stanford.bmir.protege.web.client.events.EventPollingPeriodProvider;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.filter.FilterViewImpl;
import edu.stanford.bmir.protege.web.client.help.*;
import edu.stanford.bmir.protege.web.client.login.*;
import edu.stanford.bmir.protege.web.client.logout.LogoutView;
import edu.stanford.bmir.protege.web.client.logout.LogoutViewImpl;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorViewImpl;
import edu.stanford.bmir.protege.web.client.perspective.*;
import edu.stanford.bmir.protege.web.client.place.PlaceHistoryHandlerProvider;
import edu.stanford.bmir.protege.web.client.place.WebProtegeActivityManager;
import edu.stanford.bmir.protege.web.client.place.WebProtegeActivityMapper;
import edu.stanford.bmir.protege.web.client.place.WebProtegePlaceHistoryMapper;
import edu.stanford.bmir.protege.web.client.portlet.PortletChooserView;
import edu.stanford.bmir.protege.web.client.portlet.PortletChooserViewImpl;
import edu.stanford.bmir.protege.web.client.project.*;
import edu.stanford.bmir.protege.web.client.signup.SignUpView;
import edu.stanford.bmir.protege.web.client.signup.SignUpViewImpl;
import edu.stanford.bmir.protege.web.client.topbar.GoToHomeView;
import edu.stanford.bmir.protege.web.client.topbar.GoToToHomeViewImpl;
import edu.stanford.bmir.protege.web.client.topbar.TopBarView;
import edu.stanford.bmir.protege.web.client.topbar.TopBarViewImpl;
import edu.stanford.bmir.protege.web.client.perspective.CreateFreshPerspectiveRequestHandler;
import edu.stanford.bmir.protege.web.client.projectlist.AvailableProjectView;
import edu.stanford.bmir.protege.web.client.projectlist.AvailableProjectViewImpl;
import edu.stanford.bmir.protege.web.client.projectmanager.*;
import edu.stanford.bmir.protege.web.client.user.*;
import edu.stanford.bmir.protege.web.client.app.ApplicationView;
import edu.stanford.bmir.protege.web.client.app.ApplicationViewImpl;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationPropertiesProvider;
import edu.stanford.bmir.protege.web.shared.auth.Md5MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.MessageDigestAlgorithm;

import javax.inject.Singleton;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
@Module
public class ClientApplicationModule {

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new SimpleEventBus();
    }

    @Provides
    @Singleton
    PlaceController providePlaceController(EventBus eventBus) {
        return new PlaceController(eventBus);
    }

    @Provides
    @Singleton
    PlaceHistoryHandler providePlaceHistoryHandler(PlaceHistoryHandlerProvider provider) {
        return provider.get();
    }

    @Provides
    @Singleton
    PlaceHistoryMapper providePlaceHistoryMapper(WebProtegePlaceHistoryMapper mapper) {
        return mapper;
    }

    @Provides
    @Singleton
    WebProtegePlaceHistoryMapper provideWebProtegePlaceHistoryMapper() {
        return GWT.create(WebProtegePlaceHistoryMapper.class);
    }

    @Provides
    @Singleton
    Messages provideMessages() {
        return GWT.create(Messages.class);
    }

    @Provides
    @Singleton
    WebProtegeClientBundle provideClientBundle() {
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        return WebProtegeClientBundle.BUNDLE;
    }

    @Provides
    @Singleton
    ActivityMapper provideActivityMapper(WebProtegeActivityMapper mapper) {
        return mapper;
    }

    @Provides
    @Singleton
    ActivityManager provideActivityManager(WebProtegeActivityManager manager) {
        return manager;
    }

    @Provides
    @Singleton
    HasClientApplicationProperties provideHasClientApplicationProperties(ClientApplicationProperties properties) {
        return properties;
    }

    @Provides
    @Singleton
    ClientApplicationProperties provideClientApplicationProperties(ClientApplicationPropertiesProvider provider) {
        return provider.get();
    }

    @Provides
    @Singleton
    ApplicationView provideApplicationView(ApplicationViewImpl applicationView) {
        return applicationView;
    }

    @Provides
    @Singleton
    LoggedInUserProvider provideLoggedInUserProvider(LoggedInUserManager loggedInUserManager) {
        return loggedInUserManager;
    }

    @Provides
    @Singleton
    LoggedInUserManager provideLoggedInUserManager(LoggedInUserManagerProvider provider) {
        return provider.get();
    }

    @Provides
    @Singleton
    SignInRequestHandler provideSignInRequestHandler(SignInRequestHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    SignOutRequestHandler provideSignOutRequestHandler(SignOutRequestHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    SignUpForAccountHandler provideSignUpForAccountHandler(SignUpForAccountHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    SignUpView provideSignUpView(SignUpViewImpl signUpView) {
        return signUpView;
    }

    @Provides
    @Singleton
    SignInRequiredHandler provideSignInRequiredHandler(SignInRequiredHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    ChangeEmailAddressHandler provideChangeEmailAddressHandler(ChangeEmailAddressHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    ShowUserGuideHandler provideShowUserGuideHandler(ShowUserGuideHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    ChangePasswordHandler provideChangePasswordHandler(ChangePasswordHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    ShowAboutBoxHandler provideShowAboutBoxHandler(ShowAboutBoxHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    TopBarView provideTopBarView(TopBarViewImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    ProjectManagerView provideProjectManagerView(ProjectManagerViewImpl impl) {
        return impl;
    }

    @Provides
    AvailableProjectView projectDetailsView(AvailableProjectViewImpl impl) {
        return impl;
    }

    @Provides
    LoadProjectRequestHandler providesLoadProjectRequestHandler(LoadProjectRequestHandlerImpl impl) {
        return impl;
    }

    @Provides
    LoadProjectInNewWindowRequestHandler providesLoadProjectInNewWindowRequestHandler(LoadProjectInNewWindowRequestHandlerImpl impl) {
        return impl;
    }

    @Provides
    DownloadProjectRequestHandler provideDownloadProjectRequestHandler(DownloadProjectRequestHandlerImpl impl) {
        return impl;
    }

    @Provides
    TrashManagerRequestHandler provideTrashManagerRequestHandler(TrashManagerRequestHandlerImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    LoginView provideLoginView(LoginViewImpl loginView) {
        return loginView;
    }

    @Provides
    @Singleton
    LogoutView provideLogoutView(LogoutViewImpl logoutView) {
        return logoutView;
    }

    @Provides
    @Singleton
    ResetPasswordView provideResetPasswordView(ResetPasswordViewImpl resetPasswordView) {
        return resetPasswordView;
    }

    @Provides
    @Singleton
    LoggedInUserView provideLoggedInUserView(LoggedInUserViewImpl loggedInUserView) {
        return loggedInUserView;
    }

    @Provides
    @Singleton
    HelpView provideHelpView(HelpViewImpl helpView) {
        return helpView;
    }

    @Provides
    @Singleton
    GoToHomeView provideGoToHomeView(GoToToHomeViewImpl goToToHomeView) {
        return goToToHomeView;
    }

    @Provides
    MessageDigestAlgorithm provideMessageDigestAlgorithm(Md5MessageDigestAlgorithm algorithm) {
        return algorithm;
    }

    @Provides
    @Singleton
    PerspectiveSwitcherView providePerspectiveSwitcherView(PerspectiveSwitcherViewImpl view) {
        return view;
    }

    @Provides
    PerspectiveView providePerspectiveView(PerspectiveViewImpl view) {
        return view;
    }

    @Provides
    CreateFreshPerspectiveRequestHandler provideCreateFreshPerspectiveRequestHandler(CreateFreshPerspectiveRequestHandlerImpl handler) {
        return handler;
    }

    @Provides
    EmptyPerspectiveView provideEmptyPerspectiveView(EmptyPerspectiveViewImpl view) {
        return view;
    }

    @Provides
    ForbiddenView provideForbiddenView(ForbiddenViewImpl view) {
        return view;
    }


    @Provides
    @EventPollingPeriod
    int provideEventPollingPeriod(EventPollingPeriodProvider provider) {
        return provider.get();
    }

    @Provides
    ProjectMenuView provideProjectMenuView(ProjectMenuViewImpl view) {
        return view;
    }

    @Provides
    PortletChooserView providePortletChooserView(PortletChooserViewImpl view) {
        return view;
    }

    @Provides
    ProjectView provideProjectView(ProjectViewImpl view) {
        return view;
    }

    @Provides
    ActiveProjectManager provideActiveProjectManager(ActiveProjectManagerImpl manager) {
        return manager;
    }

    @Provides
    FilterView provideFilterView(FilterViewImpl filterView) {
        return filterView;
    }

    @Provides
    PaginatorView providePaginatorView(PaginatorViewImpl view) {
        return view;
    }

    @Provides
    PortletUi providePortletUi(PortletUiImpl portletUi) {
        return portletUi;
    }

    @Provides
    CreateProjectRequestHandler provideCreateProjectRequestHandler(CreateProjectRequestHandlerImpl impl) {
        return impl;
    }

    @Provides
    UploadProjectRequestHandler provideUploadProjectRequestHandler(UploadProjectRequestHandlerImpl impl) {
        return impl;
    }
}
