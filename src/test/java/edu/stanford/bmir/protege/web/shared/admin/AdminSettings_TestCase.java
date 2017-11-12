
package edu.stanford.bmir.protege.web.shared.admin;

import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AdminSettings_TestCase {

    private AdminSettings adminSettings;

    private String applicationName = "The applicationName";

    @Mock
    private EmailAddress systemNotificationEmailAddress;

    @Mock
    private ApplicationLocation applicationLocation;

    private AccountCreationSetting accountCreationSetting = AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED;

    @Mock
    private List<UserId> accountCreators;

    private ProjectCreationSetting projectCreationSetting = ProjectCreationSetting.EMPTY_PROJECT_CREATION_NOT_ALLOWED;

    @Mock
    private List<UserId> projectCreators;

    private ProjectUploadSetting projectUploadSetting = ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED;

    @Mock
    private List<UserId> projectUploaders;

    private NotificationEmailsSetting notificationEmailsSetting = NotificationEmailsSetting.DO_NOT_SEND_NOTIFICATION_EMAILS;

    private long maxUploadSize = 1L;

    @Before
    public void setUp()
            throws Exception {
        adminSettings = new AdminSettings(applicationName,
                                          systemNotificationEmailAddress,
                                          applicationLocation,
                                          accountCreationSetting,
                                          accountCreators,
                                          projectCreationSetting,
                                          projectCreators,
                                          projectUploadSetting,
                                          projectUploaders,
                                          notificationEmailsSetting,
                                          maxUploadSize);
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationName_IsNull() {
        new AdminSettings(null,
                          systemNotificationEmailAddress,
                          applicationLocation,
                          accountCreationSetting,
                          accountCreators,
                          projectCreationSetting,
                          projectCreators,
                          projectUploadSetting,
                          projectUploaders,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_applicationName() {
        assertThat(adminSettings.getApplicationName(), is(this.applicationName));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_systemNotificationEmailAddress_IsNull() {
        new AdminSettings(applicationName,
                          null,
                          applicationLocation,
                          accountCreationSetting,
                          accountCreators,
                          projectCreationSetting,
                          projectCreators,
                          projectUploadSetting,
                          projectUploaders,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_systemNotificationEmailAddress() {
        assertThat(adminSettings.getSystemNotificationEmailAddress(), is(this.systemNotificationEmailAddress));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationLocation_IsNull() {
        new AdminSettings(applicationName,
                          systemNotificationEmailAddress,
                          null,
                          accountCreationSetting,
                          accountCreators,
                          projectCreationSetting,
                          projectCreators,
                          projectUploadSetting,
                          projectUploaders,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_applicationLocation() {
        assertThat(adminSettings.getApplicationLocation(), is(this.applicationLocation));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_accountCreationSetting_IsNull() {
        new AdminSettings(applicationName,
                          systemNotificationEmailAddress,
                          applicationLocation,
                          null,
                          accountCreators,
                          projectCreationSetting,
                          projectCreators,
                          projectUploadSetting,
                          projectUploaders,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_accountCreationSetting() {
        assertThat(adminSettings.getAccountCreationSetting(), is(this.accountCreationSetting));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_accountCreators_IsNull() {
        new AdminSettings(applicationName,
                          systemNotificationEmailAddress,
                          applicationLocation,
                          accountCreationSetting,
                          null,
                          projectCreationSetting,
                          projectCreators,
                          projectUploadSetting,
                          projectUploaders,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_accountCreators() {
        assertThat(adminSettings.getAccountCreators(), is(this.accountCreators));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectCreationSetting_IsNull() {
        new AdminSettings(applicationName,
                          systemNotificationEmailAddress,
                          applicationLocation,
                          accountCreationSetting,
                          accountCreators,
                          null,
                          projectCreators,
                          projectUploadSetting,
                          projectUploaders,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_projectCreationSetting() {
        assertThat(adminSettings.getProjectCreationSetting(), is(this.projectCreationSetting));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectCreators_IsNull() {
        new AdminSettings(applicationName,
                          systemNotificationEmailAddress,
                          applicationLocation,
                          accountCreationSetting,
                          accountCreators,
                          projectCreationSetting,
                          null,
                          projectUploadSetting,
                          projectUploaders,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_projectCreators() {
        assertThat(adminSettings.getProjectCreators(), is(this.projectCreators));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectUploadSetting_IsNull() {
        new AdminSettings(applicationName,
                          systemNotificationEmailAddress,
                          applicationLocation,
                          accountCreationSetting,
                          accountCreators,
                          projectCreationSetting,
                          projectCreators,
                          null,
                          projectUploaders,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_projectUploadSetting() {
        assertThat(adminSettings.getProjectUploadSetting(), is(this.projectUploadSetting));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectUploaders_IsNull() {
        new AdminSettings(applicationName,
                          systemNotificationEmailAddress,
                          applicationLocation,
                          accountCreationSetting,
                          accountCreators,
                          projectCreationSetting,
                          projectCreators,
                          projectUploadSetting,
                          null,
                          notificationEmailsSetting,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_projectUploaders() {
        assertThat(adminSettings.getProjectUploaders(), is(this.projectUploaders));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_notificationEmailsSetting_IsNull() {
        new AdminSettings(applicationName,
                          systemNotificationEmailAddress,
                          applicationLocation,
                          accountCreationSetting,
                          accountCreators,
                          projectCreationSetting,
                          projectCreators,
                          projectUploadSetting,
                          projectUploaders,
                          null,
                          maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_notificationEmailsSetting() {
        assertThat(adminSettings.getNotificationEmailsSetting(), is(this.notificationEmailsSetting));
    }

    @Test
    public void shouldReturnSupplied_maxUploadSize() {
        assertThat(adminSettings.getMaxUploadSize(), is(this.maxUploadSize));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(adminSettings, is(adminSettings));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull" )
    public void shouldNotBeEqualToNull() {
        assertThat(adminSettings.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(adminSettings,
                   is(new AdminSettings(applicationName,
                                        systemNotificationEmailAddress,
                                        applicationLocation,
                                        accountCreationSetting,
                                        accountCreators,
                                        projectCreationSetting,
                                        projectCreators,
                                        projectUploadSetting,
                                        projectUploaders,
                                        notificationEmailsSetting,
                                        maxUploadSize)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_applicationName() {
        assertThat(adminSettings,
                   is(not(new AdminSettings("String-c8a90a1d-4357-4486-846a-963795f4ff23" ,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            accountCreationSetting,
                                            accountCreators,
                                            projectCreationSetting,
                                            projectCreators,
                                            projectUploadSetting,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_systemNotificationEmailAddress() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            mock(EmailAddress.class),
                                            applicationLocation,
                                            accountCreationSetting,
                                            accountCreators,
                                            projectCreationSetting,
                                            projectCreators,
                                            projectUploadSetting,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_applicationLocation() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            mock(ApplicationLocation.class),
                                            accountCreationSetting,
                                            accountCreators,
                                            projectCreationSetting,
                                            projectCreators,
                                            projectUploadSetting,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_accountCreationSetting() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            AccountCreationSetting.ACCOUNT_CREATION_ALLOWED,
                                            accountCreators,
                                            projectCreationSetting,
                                            projectCreators,
                                            projectUploadSetting,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_accountCreators() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            accountCreationSetting,
                                            Collections.singletonList(mock(UserId.class)),
                                            projectCreationSetting,
                                            projectCreators,
                                            projectUploadSetting,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectCreationSetting() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            accountCreationSetting,
                                            accountCreators,
                                            ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED,
                                            projectCreators,
                                            projectUploadSetting,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectCreators() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            accountCreationSetting,
                                            accountCreators,
                                            projectCreationSetting,
                                            Collections.singletonList(mock(UserId.class)),
                                            projectUploadSetting,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectUploadSetting() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            accountCreationSetting,
                                            accountCreators,
                                            projectCreationSetting,
                                            projectCreators,
                                            ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectUploaders() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            accountCreationSetting,
                                            accountCreators,
                                            projectCreationSetting,
                                            projectCreators,
                                            projectUploadSetting,
                                            Collections.singletonList(mock(UserId.class)),
                                            notificationEmailsSetting,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_notificationEmailsSetting() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            accountCreationSetting,
                                            accountCreators,
                                            projectCreationSetting,
                                            projectCreators,
                                            projectUploadSetting,
                                            projectUploaders,
                                            NotificationEmailsSetting.SEND_NOTIFICATION_EMAILS,
                                            maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_maxUploadSize() {
        assertThat(adminSettings,
                   is(not(new AdminSettings(applicationName,
                                            systemNotificationEmailAddress,
                                            applicationLocation,
                                            accountCreationSetting,
                                            accountCreators,
                                            projectCreationSetting,
                                            projectCreators,
                                            projectUploadSetting,
                                            projectUploaders,
                                            notificationEmailsSetting,
                                            2L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(adminSettings.hashCode(),
                   is(new AdminSettings(applicationName,
                                        systemNotificationEmailAddress,
                                        applicationLocation,
                                        accountCreationSetting,
                                        accountCreators,
                                        projectCreationSetting,
                                        projectCreators,
                                        projectUploadSetting,
                                        projectUploaders,
                                        notificationEmailsSetting,
                                        maxUploadSize).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(adminSettings.toString(), Matchers.startsWith("AdminSettings" ));
    }

}
