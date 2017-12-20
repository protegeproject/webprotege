package edu.stanford.bmir.protege.web.server.app;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.app.AccountCreationSetting;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.app.ProjectCreationSetting;
import edu.stanford.bmir.protege.web.shared.app.ProjectUploadSetting;
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
import static edu.stanford.bmir.protege.web.shared.app.AccountCreationSetting.ACCOUNT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.NotificationEmailsSetting.SEND_NOTIFICATION_EMAILS;
import static edu.stanford.bmir.protege.web.shared.app.ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectCreationSetting.EMPTY_PROJECT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Mar 2017
 */
@ApplicationSingleton
public class ApplicationSettingsManager {

    private final AccessManager accessManager;

    private final ApplicationPreferencesStore settingsStore;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public ApplicationSettingsManager(@Nonnull AccessManager accessManager,
                                      @Nonnull ApplicationPreferencesStore settingsStore) {
        this.accessManager = checkNotNull(accessManager);
        this.settingsStore = checkNotNull(settingsStore);
    }

    @Nonnull
    public ApplicationSettings getApplicationSettings() {
        try {
            readLock.lock();
            ApplicationPreferences applicationPreferences = settingsStore.getApplicationPreferences();
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
            return new ApplicationSettings(
                    applicationPreferences.getApplicationName(),
                    new EmailAddress(applicationPreferences.getSystemNotificationEmailAddress()),
                    applicationPreferences.getApplicationLocation(),
                    accountCreationSetting,
                    ImmutableList.of(),
                    projectCreationSetting,
                    ImmutableList.of(),
                    projectUploadSetting,
                    ImmutableList.of(),
                    SEND_NOTIFICATION_EMAILS,
                    applicationPreferences.getMaxUploadSize()
            );
        } finally {
            readLock.unlock();
        }
    }

    public void setApplicationSettings(@Nonnull ApplicationSettings settings) {
        try {
            writeLock.lock();
            ApplicationPreferences applicationPreferences = new ApplicationPreferences(
                    settings.getApplicationName(),
                    settings.getSystemNotificationEmailAddress().getEmailAddress(),
                    settings.getApplicationLocation(),
                    settings.getMaxUploadSize()
            );
            settingsStore.setApplicationPreferences(applicationPreferences);
            Set<RoleId> guestRoleIds = new HashSet<>(accessManager.getAssignedRoles(forGuestUser(),
                                                                                    ApplicationResource.get()));
            if(settings.getAccountCreationSetting() == ACCOUNT_CREATION_ALLOWED) {
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
            if(settings.getProjectCreationSetting() == EMPTY_PROJECT_CREATION_ALLOWED) {
                roleIds.add(PROJECT_CREATOR.getRoleId());
            }
            else {
                roleIds.remove(PROJECT_CREATOR.getRoleId());
            }

            if(settings.getProjectUploadSetting() == PROJECT_UPLOAD_ALLOWED) {
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
