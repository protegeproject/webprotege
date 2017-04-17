package edu.stanford.bmir.protege.web.client.admin;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.admin.*;
import edu.stanford.bmir.protege.web.shared.admin.AdminSettings;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_APPLICATION_SETTINGS;
import static edu.stanford.bmir.protege.web.shared.admin.AccountCreationSetting.ACCOUNT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.NotificationEmailsSetting.DO_NOT_SEND_NOTIFICATION_EMAILS;
import static edu.stanford.bmir.protege.web.shared.admin.NotificationEmailsSetting.SEND_NOTIFICATION_EMAILS;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectCreationSetting.EMPTY_PROJECT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
@ApplicationSingleton
public class AdminPresenter implements Presenter {

    public static final RegExp HOST_REGEXP = RegExp.compile("^(?=.{1,255}$)[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?(?:\\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?)*\\.?$");

    public static final RegExp PATH_REGEXP = RegExp.compile("(^\\/[/.a-zA-Z0-9-]+$)|^$");

    private final AdminView view;

    private final LoggedInUserManager loggedInUserManager;

    private final ForbiddenView forbiddenView;

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public AdminPresenter(AdminView view,
                          LoggedInUserManager loggedInUserManager,
                          ForbiddenView forbiddenView,
                          DispatchServiceManager dispatchServiceManager) {
        this.view = view;
        this.loggedInUserManager = loggedInUserManager;
        this.forbiddenView = forbiddenView;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        if(!loggedInUserManager.isAllowedApplicationAction(EDIT_APPLICATION_SETTINGS)) {
            container.setWidget(forbiddenView);
        }
        else {
            container.setWidget(view);
            dispatchServiceManager.execute(new GetAdminSettingsAction(),
                                           result -> {
                                               displaySettings(result.getAdminSettings());
                                           });
        }
        view.setApplySettingsHandler(this::applySettings);
        view.setRebuildPermissionsHandler(this::rebuildPermissions);
    }

    private void displaySettings(AdminSettings adminSettings) {
        view.setApplicationName(adminSettings.getApplicationName());
        view.setApplicationLogo(adminSettings.getCustomLogoUrl());
        view.setAdminEmailAddress(adminSettings.getAdminEmailAddress().getEmailAddress());
        view.setAccountCreationAllowed(adminSettings.getAccountCreationSetting() == ACCOUNT_CREATION_ALLOWED);
        view.setProjectCreationAllowed(adminSettings.getProjectCreationSetting() == EMPTY_PROJECT_CREATION_ALLOWED);
        view.setProjectUploadAllowed(adminSettings.getProjectUploadSetting() == PROJECT_UPLOAD_ALLOWED);
        view.setNotificationEmailsEnabled(adminSettings.getNotificationEmailsSetting() == SEND_NOTIFICATION_EMAILS);
        SchemeValue scheme = SchemeValue.valueOf(adminSettings.getApplicationLocation().getScheme().toUpperCase());
        view.setScheme(scheme);
        view.setHost(adminSettings.getApplicationLocation().getHost());
        view.setPath(adminSettings.getApplicationLocation().getPath());
        int port = adminSettings.getApplicationLocation().getPort();
        if(scheme.getDefaultPort() == port) {
            view.setPort("");
        }
        else {
            view.setPort(Integer.toString(port));
        }

        if(adminSettings.getMaxUploadSize() == Long.MAX_VALUE) {
            view.setMaxUploadSize("");
        }
        else {
            String maxUploadSize = Long.toString(adminSettings.getMaxUploadSize() / (1024 * 1024));
            view.setMaxUploadSize(maxUploadSize);
        }

    }

    private void applySettings() {
        AdminSettings adminSettings = new AdminSettings(
                view.getApplicationName(),
                view.getApplicationLogo(),
                new EmailAddress(view.getAdminEmailAddress()),
                new ApplicationLocation(view.getScheme().name().toLowerCase(),
                                        getHostNameFromView(),
                                        getPathFromView(),
                                        getPortFromView()),
                view.isAccountCreationAllowed() ? ACCOUNT_CREATION_ALLOWED : ACCOUNT_CREATION_NOT_ALLOWED,
                Collections.emptyList(),
                view.isProjectCreationAllowed() ? EMPTY_PROJECT_CREATION_ALLOWED : EMPTY_PROJECT_CREATION_NOT_ALLOWED,
                Collections.emptyList(),
                view.isProjectUploadAllowed() ? PROJECT_UPLOAD_ALLOWED : PROJECT_UPLOAD_NOT_ALLOWED,
                Collections.emptyList(),
                view.isNotificationEmailsEnabled() ? SEND_NOTIFICATION_EMAILS : DO_NOT_SEND_NOTIFICATION_EMAILS,
                parseMaxUploadSize()
        );
        dispatchServiceManager.execute(new SetAdminSettingsAction(adminSettings),
                                       result -> MessageBox.showMessage("Settings applied", "The application settings have successfully been applied"));
    }

    private long parseMaxUploadSize() {
        String maxUploadSize = view.getMaxUploadSize();
        if(maxUploadSize.isEmpty()) {
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
                                       new DispatchServiceCallbackWithProgressDisplay<RebuildPermissionsResult>() {
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
        String hostName = view.getHost();
        if(HOST_REGEXP.test(hostName)) {
            return hostName;
        }
        else {
            return "";
        }
    }

    private String getPathFromView() {
        String enteredValue = view.getPath();
        if(enteredValue.isEmpty()) {
            return "";
        }
        if(PATH_REGEXP.test(enteredValue)) {
            return enteredValue;
        }
        else {
            return "";
        }
    }

    private int getPortFromView() {
        String portValue = view.getPort();
        try {
            return Integer.parseInt(portValue);
        } catch (NumberFormatException e) {
            return view.getScheme().getDefaultPort();
        }
    }


}
