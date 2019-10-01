package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.app.ApplicationPreferences;
import edu.stanford.bmir.protege.web.server.app.ApplicationPreferencesStore;
import edu.stanford.bmir.protege.web.server.app.ApplicationSettingsManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static edu.stanford.bmir.protege.web.server.access.Subject.forAnySignedInUser;
import static edu.stanford.bmir.protege.web.server.access.Subject.forGuestUser;
import static edu.stanford.bmir.protege.web.shared.app.AccountCreationSetting.ACCOUNT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectCreationSetting.EMPTY_PROJECT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.app.ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationPreferencesManager_TestCase {

    public static final String THE_APP_NAME = "TheAppName";

    public static final String THE_SYSTEM_NOTIFICATION_EMAIL_ADDRESS = "TheSystemNotificationEmailAddress";

    private ApplicationSettingsManager manager;

    @Mock
    private AccessManager accessManager;

    @Mock
    private ApplicationPreferencesStore applicationPreferencesStore;

    @Mock
    private ApplicationPreferences applicationPreferences;

    @Mock
    private ApplicationLocation applicationLocation;

    @Before
    public void setUp() throws Exception {
        manager = new ApplicationSettingsManager(accessManager,
                                                 applicationPreferencesStore);

        when(applicationPreferences.getApplicationName()).thenReturn(THE_APP_NAME);
        when(applicationPreferences.getSystemNotificationEmailAddress()).thenReturn(THE_SYSTEM_NOTIFICATION_EMAIL_ADDRESS);
        when(applicationPreferences.getApplicationLocation()).thenReturn(applicationLocation);
        when(applicationPreferencesStore.getApplicationPreferences()).thenReturn(applicationPreferences);
    }

    @Test
    public void shouldGetApplicationSettings() {
        ApplicationSettings applicationSettings = manager.getApplicationSettings();
        assertThat(applicationSettings.getApplicationName(), is(THE_APP_NAME));
        assertThat(applicationSettings.getSystemNotificationEmailAddress().getEmailAddress(), is(THE_SYSTEM_NOTIFICATION_EMAIL_ADDRESS));
        assertThat(applicationSettings.getApplicationLocation(), is(applicationLocation));
    }

    @Test
    public void shouldGetAccountCreationNotAllowed() {
        ApplicationSettings applicationSettings = manager.getApplicationSettings();
        assertThat(applicationSettings.getAccountCreationSetting(), is(ACCOUNT_CREATION_NOT_ALLOWED));
    }

    @Test
    public void shouldGetAccountCreationAllowed() {
        when(accessManager.hasPermission(forGuestUser(),
                                         ApplicationResource.get(),
                                         BuiltInAction.CREATE_ACCOUNT)).thenReturn(true);
        ApplicationSettings applicationSettings = manager.getApplicationSettings();
        assertThat(applicationSettings.getAccountCreationSetting(), is(ACCOUNT_CREATION_ALLOWED));
    }

    @Test
    public void shouldGetProjectCreationNotAllowed() {
        ApplicationSettings applicationSettings = manager.getApplicationSettings();
        assertThat(applicationSettings.getProjectCreationSetting(), is(EMPTY_PROJECT_CREATION_NOT_ALLOWED));
    }

    @Test
    public void shouldGetProjectCreationAllowed() {
        when(accessManager.hasPermission(forAnySignedInUser(),
                                         ApplicationResource.get(),
                                         BuiltInAction.CREATE_EMPTY_PROJECT)).thenReturn(true);
        ApplicationSettings applicationSettings = manager.getApplicationSettings();
        assertThat(applicationSettings.getProjectCreationSetting(), is(EMPTY_PROJECT_CREATION_ALLOWED));
    }

    @Test
    public void shouldGetProjectUploadNotAllowed() {
        ApplicationSettings applicationSettings = manager.getApplicationSettings();
        assertThat(applicationSettings.getProjectUploadSetting(), is(PROJECT_UPLOAD_NOT_ALLOWED));
    }

    @Test
    public void shouldGetProjectUploadAllowed() {
        when(accessManager.hasPermission(forAnySignedInUser(),
                                         ApplicationResource.get(),
                                         BuiltInAction.UPLOAD_PROJECT)).thenReturn(true);
        ApplicationSettings applicationSettings = manager.getApplicationSettings();
        assertThat(applicationSettings.getProjectUploadSetting(), is(PROJECT_UPLOAD_ALLOWED));
    }
}
