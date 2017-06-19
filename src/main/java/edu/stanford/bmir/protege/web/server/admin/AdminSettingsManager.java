package edu.stanford.bmir.protege.web.server.admin;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.admin.AccountCreationSetting;
import edu.stanford.bmir.protege.web.shared.admin.AdminSettings;
import edu.stanford.bmir.protege.web.shared.admin.ProjectCreationSetting;
import edu.stanford.bmir.protege.web.shared.admin.ProjectUploadSetting;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.access.Subject.forAnySignedInUser;
import static edu.stanford.bmir.protege.web.server.access.Subject.forGuestUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_EMPTY_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.UPLOAD_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInRole.*;
import static edu.stanford.bmir.protege.web.shared.admin.AccountCreationSetting.ACCOUNT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.NotificationEmailsSetting.SEND_NOTIFICATION_EMAILS;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectCreationSetting.EMPTY_PROJECT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Mar 2017
 */
@ApplicationSingleton
public class AdminSettingsManager {

    private final AccessManager accessManager;

    private final ApplicationSettingsManager appSettingsManager;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public AdminSettingsManager(@Nonnull AccessManager accessManager,
                                @Nonnull ApplicationSettingsManager appSettingsManager) {
        this.accessManager = checkNotNull(accessManager);
        this.appSettingsManager = checkNotNull(appSettingsManager);
    }

    @Nonnull
    public AdminSettings getAdminSettings() {
        try {
            readLock.lock();
            ApplicationSettings appSettings = appSettingsManager.getApplicationSettings();
            AccountCreationSetting accountCreationSetting;
            boolean canCreateAccounts = accessManager.hasPermission(forGuestUser(),
                                                                    ApplicationResource.get(),
                                                                    BuiltInAction.CREATE_ACCOUNT);
            if(canCreateAccounts) {
                accountCreationSetting = ACCOUNT_CREATION_ALLOWED;
            }
            else {
                accountCreationSetting = ACCOUNT_CREATION_NOT_ALLOWED;
            }
            ProjectCreationSetting projectCreationSetting;
            boolean canCreateEmptyProject = accessManager.hasPermission(forAnySignedInUser(),
                                                                        ApplicationResource.get(),
                                                                        CREATE_EMPTY_PROJECT);
            if(canCreateEmptyProject) {
                projectCreationSetting = EMPTY_PROJECT_CREATION_ALLOWED;
            }
            else {
                projectCreationSetting = EMPTY_PROJECT_CREATION_NOT_ALLOWED;
            }
            ProjectUploadSetting projectUploadSetting;
            boolean canUploadProject = accessManager.hasPermission(forAnySignedInUser(),
                                                                   ApplicationResource.get(),
                                                                   UPLOAD_PROJECT);
            if(canUploadProject) {
                projectUploadSetting = PROJECT_UPLOAD_ALLOWED;
            }
            else {
                projectUploadSetting = PROJECT_UPLOAD_NOT_ALLOWED;
            }
            return new AdminSettings(
                    appSettings.getApplicationName(),
                    appSettings.getCustomLogoUrl(),
                    new EmailAddress(appSettings.getAdminEmailAddress()),
                    appSettings.getApplicationLocation(),
                    accountCreationSetting,
                    ImmutableList.of(),
                    projectCreationSetting,
                    ImmutableList.of(),
                    projectUploadSetting,
                    ImmutableList.of(),
                    SEND_NOTIFICATION_EMAILS,
                    appSettings.getMaxUploadSize()
            );
        } finally {
            readLock.unlock();
        }
    }

    public void setAdminSettings(@Nonnull AdminSettings adminSettings) {
        try {
            writeLock.lock();
            ApplicationSettings applicationSettings = new ApplicationSettings(
                    adminSettings.getApplicationName(),
                    adminSettings.getCustomLogoUrl(),
                    adminSettings.getAdminEmailAddress().getEmailAddress(),
                    adminSettings.getApplicationLocation(),
                    adminSettings.getMaxUploadSize()
            );
            appSettingsManager.setApplicationSettings(applicationSettings);
            Set<RoleId> guestRoleIds = new HashSet<>(accessManager.getAssignedRoles(forGuestUser(),
                                                                                    ApplicationResource.get()));
            if(adminSettings.getAccountCreationSetting() == ACCOUNT_CREATION_ALLOWED) {
                guestRoleIds.add(ACCOUNT_CREATOR.getRoleId());
            }
            else {
                guestRoleIds.remove(ACCOUNT_CREATOR.getRoleId());
            }
            accessManager.setAssignedRoles(forGuestUser(),
                                           ApplicationResource.get(),
                                           guestRoleIds);


            Set<RoleId> roleIds = new HashSet<>(accessManager.getAssignedRoles(forAnySignedInUser(),
                                                                               ApplicationResource.get()));
            if(adminSettings.getProjectCreationSetting() == EMPTY_PROJECT_CREATION_ALLOWED) {
                roleIds.add(PROJECT_CREATOR.getRoleId());
            }
            else {
                roleIds.remove(PROJECT_CREATOR.getRoleId());
            }

            if(adminSettings.getProjectUploadSetting() == PROJECT_UPLOAD_ALLOWED) {
                roleIds.add(PROJECT_UPLOADER.getRoleId());
            }
            else {
                roleIds.remove(PROJECT_UPLOADER.getRoleId());
            }
            accessManager.setAssignedRoles(forAnySignedInUser(),
                                           ApplicationResource.get(),
                                           roleIds);
        } finally {
            writeLock.unlock();
        }
    }


}
