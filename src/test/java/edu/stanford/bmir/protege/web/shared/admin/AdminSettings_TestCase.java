
package edu.stanford.bmir.protege.web.shared.admin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AdminSettings_TestCase {

    private AdminSettings adminSettings;

    private String applicationName = "The applicationName";

    private String customLogoUrl = "The customLogoUrl";

    @Mock
    private EmailAddress adminEmailAddress;

    @Mock
    private ApplicationLocation applicationLocation;

    private AccountCreationSetting accountCreationSetting = AccountCreationSetting.ACCOUNT_CREATION_ALLOWED;

    @Mock
    private List<UserId> accountCreators = singletonList(mock(UserId.class));

    private ProjectCreationSetting projectCreationSetting = ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED;

    @Mock
    private List<UserId> projectCreators = singletonList(mock(UserId.class));

    private ProjectUploadSetting projectUploadSetting = ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED;

    @Mock
    private List<UserId> projectUploaders = singletonList(mock(UserId.class));

    private NotificationEmailsSetting notificationEmailsSetting = NotificationEmailsSetting.SEND_NOTIFICATION_EMAILS;

    @Before
    public void setUp()
        throws Exception
    {
        adminSettings = new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationName_IsNull() {
        new AdminSettings(null, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_applicationName() {
        assertThat(adminSettings.getApplicationName(), is(this.applicationName));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_customLogoUrl_IsNull() {
        new AdminSettings(applicationName, null, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_customLogoUrl() {
        assertThat(adminSettings.getCustomLogoUrl(), is(this.customLogoUrl));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_adminEmailAddress_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, null, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_adminEmailAddress() {
        assertThat(adminSettings.getAdminEmailAddress(), is(this.adminEmailAddress));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationLocation_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, null, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_applicationLocation() {
        assertThat(adminSettings.getApplicationLocation(), is(this.applicationLocation));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_accountCreationSetting_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, null, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_accountCreationSetting() {
        assertThat(adminSettings.getAccountCreationSetting(), is(this.accountCreationSetting));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_accountCreators_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, null, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_accountCreators() {
        assertThat(adminSettings.getAccountCreators(), is(this.accountCreators));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectCreationSetting_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, null, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_projectCreationSetting() {
        assertThat(adminSettings.getProjectCreationSetting(), is(this.projectCreationSetting));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectCreators_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, null, projectUploadSetting, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_projectCreators() {
        assertThat(adminSettings.getProjectCreators(), is(this.projectCreators));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectUploadSetting_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, null, projectUploaders, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_projectUploadSetting() {
        assertThat(adminSettings.getProjectUploadSetting(), is(this.projectUploadSetting));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectUploaders_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, null, notificationEmailsSetting);
    }

    @Test
    public void shouldReturnSupplied_projectUploaders() {
        assertThat(adminSettings.getProjectUploaders(), is(this.projectUploaders));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_notificationEmailsSetting_IsNull() {
        new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, null);
    }

    @Test
    public void shouldReturnSupplied_notificationEmailsSetting() {
        assertThat(adminSettings.getNotificationEmailsSetting(), is(this.notificationEmailsSetting));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(adminSettings, is(adminSettings));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(adminSettings.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(adminSettings, is(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_applicationName() {
        assertThat(adminSettings, is(not(new AdminSettings("String-114adece-bdde-4100-839d-10ac38097625", customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_customLogoUrl() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, "String-f5caad45-6853-4ff9-aadd-99119c5dc7ed", adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_adminEmailAddress() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, mock(EmailAddress.class), applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_applicationLocation() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, mock(ApplicationLocation.class), accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_accountCreationSetting() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_accountCreators() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, singletonList(mock(UserId.class)), projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectCreationSetting() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, ProjectCreationSetting.EMPTY_PROJECT_CREATION_NOT_ALLOWED, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectCreators() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, singletonList(mock(UserId.class)), projectUploadSetting, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectUploadSetting() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED, projectUploaders, notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectUploaders() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, singletonList(mock(UserId.class)), notificationEmailsSetting))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_notificationEmailsSetting() {
        assertThat(adminSettings, is(not(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, NotificationEmailsSetting.DO_NOT_SEND_NOTIFICATION_EMAILS))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(adminSettings.hashCode(), is(new AdminSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, accountCreationSetting, accountCreators, projectCreationSetting, projectCreators, projectUploadSetting, projectUploaders, notificationEmailsSetting).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(adminSettings.toString(), startsWith("AdminSettings"));
    }

}
