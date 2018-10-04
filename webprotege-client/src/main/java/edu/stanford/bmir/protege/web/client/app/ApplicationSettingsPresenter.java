package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.place.ProjectListPlace;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_APPLICATION_SETTINGS;
import static edu.stanford.bmir.protege.web.shared.app.AccountCreationSetting.ACCOUNT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.NotificationEmailsSetting.DO_NOT_SEND_NOTIFICATION_EMAILS;
import static edu.stanford.bmir.protege.web.shared.app.NotificationEmailsSetting.SEND_NOTIFICATION_EMAILS;
import static edu.stanford.bmir.protege.web.shared.app.ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectCreationSetting.EMPTY_PROJECT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
@ApplicationSingleton
public class ApplicationSettingsPresenter implements Presenter {

    public static final RegExp HOST_REGEXP = RegExp.compile("^(?=.{1,255}$)[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?(?:\\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?)*\\.?$");

    public static final RegExp PATH_REGEXP = RegExp.compile("(^\\/[/.a-zA-Z0-9-]+$)|^$");

    @Nonnull
    private final SystemDetailsView systemDetailsView;

    @Nonnull
    private final ApplicationUrlView applicationUrlView;

    @Nonnull
    private final GlobalPermissionSettingsView permissionsView;

    @Nonnull
    private final EmailNotificationSettingsView emailNotificationSettingsView;

    @Nonnull
    private final LoggedInUserManager loggedInUserManager;

    @Nonnull
    private final ForbiddenView forbiddenView;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public ApplicationSettingsPresenter(@Nonnull SystemDetailsView systemDetailsView,
                                        @Nonnull ApplicationUrlView applicationUrlView,
                                        @Nonnull GlobalPermissionSettingsView permissionsView,
                                        @Nonnull EmailNotificationSettingsView emailNotificationSettingsView,
                                        @Nonnull LoggedInUserManager loggedInUserManager,
                                        @Nonnull ForbiddenView forbiddenView,
                                        @Nonnull DispatchServiceManager dispatchServiceManager,
                                        @Nonnull SettingsPresenter settingsPresenter,
                                        @Nonnull Messages messages,
                                        @Nonnull MessageBox messageBox, @Nonnull DispatchErrorMessageDisplay errorDisplay, @Nonnull ProgressDisplay progressDisplay) {
        this.systemDetailsView = checkNotNull(systemDetailsView);
        this.applicationUrlView = checkNotNull(applicationUrlView);
        this.permissionsView = checkNotNull(permissionsView);
        this.emailNotificationSettingsView = checkNotNull(emailNotificationSettingsView);
        this.loggedInUserManager = checkNotNull(loggedInUserManager);
        this.forbiddenView = checkNotNull(forbiddenView);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.messages = checkNotNull(messages);
        this.messageBox = messageBox;
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        if (!loggedInUserManager.isAllowedApplicationAction(EDIT_APPLICATION_SETTINGS)) {
            container.setWidget(forbiddenView);
        }
        else {
            settingsPresenter.setNextPlace(Optional.of(new ProjectListPlace()));
            settingsPresenter.start(container);
            settingsPresenter.setSettingsTitle(messages.applicationSettings());
            settingsPresenter.addSection(messages.applicationSettings_SystemSettings()).setWidget(systemDetailsView);
            settingsPresenter.addSection(messages.applicationSettings_ApplicationUrl()).setWidget(applicationUrlView);
            settingsPresenter.addSection(messages.applicationSettings_GlobalPermissions()).setWidget(permissionsView);
            settingsPresenter.addSection(messages.applicationSettings_EmailNotifications()).setWidget(emailNotificationSettingsView);
            settingsPresenter.setApplySettingsHandler(this::applySettings);
            settingsPresenter.setCancelSettingsHandler(this::cancelSettings);
            dispatchServiceManager.execute(new GetApplicationSettingsAction(),
                                           result -> displaySettings(result.getApplicationSettings()));
        }
        // TODO:
//        view.setRebuildPermissionsHandler(this::rebuildPermissions);
    }

    private void displaySettings(ApplicationSettings applicationSettings) {
        systemDetailsView.setApplicationName(applicationSettings.getApplicationName());
        systemDetailsView.setSystemNotificationEmailAddress(applicationSettings.getSystemNotificationEmailAddress().getEmailAddress());
        permissionsView.setAccountCreationAllowed(applicationSettings.getAccountCreationSetting() == ACCOUNT_CREATION_ALLOWED);
        permissionsView.setProjectCreationAllowed(applicationSettings.getProjectCreationSetting() == EMPTY_PROJECT_CREATION_ALLOWED);
        permissionsView.setProjectUploadAllowed(applicationSettings.getProjectUploadSetting() == PROJECT_UPLOAD_ALLOWED);
        emailNotificationSettingsView.setNotificationEmailsEnabled(applicationSettings.getNotificationEmailsSetting() == SEND_NOTIFICATION_EMAILS);
        SchemeValue scheme = SchemeValue.valueOf(applicationSettings.getApplicationLocation().getScheme().toUpperCase());
        applicationUrlView.setScheme(scheme);
        applicationUrlView.setHost(applicationSettings.getApplicationLocation().getHost());
        applicationUrlView.setPath(applicationSettings.getApplicationLocation().getPath());
        int port = applicationSettings.getApplicationLocation().getPort();
        if (scheme.getDefaultPort() == port) {
            applicationUrlView.setPort("");
        }
        else {
            applicationUrlView.setPort(Integer.toString(port));
        }

        if (applicationSettings.getMaxUploadSize() == Long.MAX_VALUE) {
            permissionsView.setMaxUploadSize("");
        }
        else {
            String maxUploadSize = Long.toString(applicationSettings.getMaxUploadSize() / (1024 * 1024));
            permissionsView.setMaxUploadSize(maxUploadSize);
        }

    }

    private void applySettings() {
        ApplicationSettings applicationSettings = new ApplicationSettings(
                systemDetailsView.getApplicationName(),
                new EmailAddress(systemDetailsView.getSystemNotificationEmailAddress()),
                new ApplicationLocation(applicationUrlView.getScheme().name().toLowerCase(),
                                        getHostNameFromView(),
                                        getPathFromView(),
                                        getPortFromView()),
                permissionsView.isAccountCreationAllowed() ? ACCOUNT_CREATION_ALLOWED : ACCOUNT_CREATION_NOT_ALLOWED,
                Collections.emptyList(),
                permissionsView.isProjectCreationAllowed() ? EMPTY_PROJECT_CREATION_ALLOWED : EMPTY_PROJECT_CREATION_NOT_ALLOWED,
                Collections.emptyList(),
                permissionsView.isProjectUploadAllowed() ? PROJECT_UPLOAD_ALLOWED : PROJECT_UPLOAD_NOT_ALLOWED,
                Collections.emptyList(),
                emailNotificationSettingsView.isNotificationEmailsEnabled() ? SEND_NOTIFICATION_EMAILS : DO_NOT_SEND_NOTIFICATION_EMAILS,
                parseMaxUploadSize()
        );
        dispatchServiceManager.execute(new SetApplicationSettingsAction(applicationSettings),
                                       result -> messageBox.showMessage("Settings applied",
                                                                        "The application settings have successfully been applied",
                                                                        settingsPresenter::goToNextPlace));
    }

    private void cancelSettings() {
        settingsPresenter.goToNextPlace();
    }

    private long parseMaxUploadSize() {
        String maxUploadSize = permissionsView.getMaxUploadSize();
        if (maxUploadSize.isEmpty()) {
            return Long.MAX_VALUE;
        }
        try {
            return Long.parseLong(maxUploadSize) * 1024 * 1024;
        } catch (NumberFormatException e) {
            return Long.MAX_VALUE;
        }
    }

    private void rebuildPermissions() {
        dispatchServiceManager.execute(new RebuildPermissionsAction(),
                                       new DispatchServiceCallbackWithProgressDisplay<RebuildPermissionsResult>(errorDisplay, progressDisplay) {
                                           @Override
                                           public String getProgressDisplayTitle() {
                                               return "Rebuilding permissions";
                                           }

                                           @Override
                                           public String getProgressDisplayMessage() {
                                               return "Please wait";
                                           }
                                       });
    }

    private String getHostNameFromView() {
        String hostName = applicationUrlView.getHost();
        if (HOST_REGEXP.test(hostName)) {
            return hostName;
        }
        else {
            return "";
        }
    }

    private String getPathFromView() {
        String enteredValue = applicationUrlView.getPath();
        if (enteredValue.isEmpty()) {
            return "";
        }
        if (PATH_REGEXP.test(enteredValue)) {
            return enteredValue;
        }
        else {
            return "";
        }
    }

    private int getPortFromView() {
        String portValue = applicationUrlView.getPort();
        try {
            return Integer.parseInt(portValue);
        } catch (NumberFormatException e) {
            return applicationUrlView.getScheme().getDefaultPort();
        }
    }


}
