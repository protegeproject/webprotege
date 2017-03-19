package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.admin.AdminSettings;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static edu.stanford.bmir.protege.web.server.access.Subject.forAnySignedInUser;
import static edu.stanford.bmir.protege.web.shared.admin.AccountCreationSetting.ACCOUNT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED;
import static edu.stanford.bmir.protege.web.shared.admin.ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminSettingsManager_TestCase {

    public static final String THE_APP_NAME = "TheAppName";

    public static final String THE_LOGO_URL = "TheLogoUrl";

    public static final String THE_ADMIN_EMAIL_ADDRESS = "TheAdminEmailAddress";

    private AdminSettingsManager manager;

    @Mock
    private AccessManager accessManager;

    @Mock
    private ApplicationSettingsManager applicationSettingsManager;

    @Mock
    private ApplicationSettings applicationSettings;

    @Mock
    private ApplicationLocation applicationLocation;

    @Before
    public void setUp() throws Exception {
        manager = new AdminSettingsManager(accessManager,
                                           applicationSettingsManager);

        when(applicationSettings.getApplicationName()).thenReturn(THE_APP_NAME);
        when(applicationSettings.getCustomLogoUrl()).thenReturn(THE_LOGO_URL);
        when(applicationSettings.getAdminEmailAddress()).thenReturn(THE_ADMIN_EMAIL_ADDRESS);
        when(applicationSettings.getApplicationLocation()).thenReturn(applicationLocation);
        when(applicationSettingsManager.getApplicationSettings()).thenReturn(applicationSettings);
    }

    @Test
    public void shouldGetApplicationSettings() {
        AdminSettings adminSettings = manager.getAdminSettings();
        assertThat(adminSettings.getApplicationName(), is(THE_APP_NAME));
        assertThat(adminSettings.getCustomLogoUrl(), is(THE_LOGO_URL));
        assertThat(adminSettings.getAdminEmailAddress().getEmailAddress(), is(THE_ADMIN_EMAIL_ADDRESS));
        assertThat(adminSettings.getApplicationLocation(), is(applicationLocation));
    }

    @Test
    public void shouldGetAccountCreationNotAllowed() {
        AdminSettings adminSettings = manager.getAdminSettings();
        assertThat(adminSettings.getAccountCreationSetting(), is(ACCOUNT_CREATION_NOT_ALLOWED));
    }

    @Test
    public void shouldGetAccountCreationAllowed() {
        when(accessManager.hasPermission(forAnySignedInUser(),
                                         ApplicationResource.get(),
                                         BuiltInAction.CREATE_ACCOUNT.getActionId())).thenReturn(true);
        AdminSettings adminSettings = manager.getAdminSettings();
        assertThat(adminSettings.getAccountCreationSetting(), is(ACCOUNT_CREATION_ALLOWED));
    }

    @Test
    public void shouldGetProjectCreationNotAllowed() {
        AdminSettings adminSettings = manager.getAdminSettings();
        assertThat(adminSettings.getProjectCreationSetting(), is(EMPTY_PROJECT_CREATION_ALLOWED));
    }

    @Test
    public void shouldGetProjectCreationAllowed() {
        when(accessManager.hasPermission(forAnySignedInUser(),
                                         ApplicationResource.get(),
                                         BuiltInAction.CREATE_EMPTY_PROJECT.getActionId())).thenReturn(true);
        AdminSettings adminSettings = manager.getAdminSettings();
        assertThat(adminSettings.getAccountCreationSetting(), is(EMPTY_PROJECT_CREATION_ALLOWED));
    }

    @Test
    public void shouldGetProjectUploadNotAllowed() {
        AdminSettings adminSettings = manager.getAdminSettings();
        assertThat(adminSettings.getProjectUploadSetting(), is(PROJECT_UPLOAD_NOT_ALLOWED));
    }

    @Test
    public void shouldGetProjectUploadAllowed() {
        when(accessManager.hasPermission(forAnySignedInUser(),
                                         ApplicationResource.get(),
                                         BuiltInAction.UPLOAD_PROJECT.getActionId())).thenReturn(true);
        AdminSettings adminSettings = manager.getAdminSettings();
        assertThat(adminSettings.getProjectUploadSetting(), is(PROJECT_UPLOAD_ALLOWED));
    }
}
