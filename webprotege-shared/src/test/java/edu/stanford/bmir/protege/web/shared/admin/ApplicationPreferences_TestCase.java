
package edu.stanford.bmir.protege.web.shared.admin;

import edu.stanford.bmir.protege.web.shared.app.*;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationPreferences_TestCase {

    private ApplicationSettings applicationSettings;

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
        applicationSettings = new ApplicationSettings(applicationName,
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
        new ApplicationSettings(null,
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
        assertThat(applicationSettings.getApplicationName(), is(this.applicationName));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_systemNotificationEmailAddress_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getSystemNotificationEmailAddress(), is(this.systemNotificationEmailAddress));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationLocation_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getApplicationLocation(), is(this.applicationLocation));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_accountCreationSetting_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getAccountCreationSetting(), is(this.accountCreationSetting));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_accountCreators_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getAccountCreators(), is(this.accountCreators));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectCreationSetting_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getProjectCreationSetting(), is(this.projectCreationSetting));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectCreators_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getProjectCreators(), is(this.projectCreators));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectUploadSetting_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getProjectUploadSetting(), is(this.projectUploadSetting));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectUploaders_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getProjectUploaders(), is(this.projectUploaders));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_notificationEmailsSetting_IsNull() {
        new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.getNotificationEmailsSetting(), is(this.notificationEmailsSetting));
    }

    @Test
    public void shouldReturnSupplied_maxUploadSize() {
        assertThat(applicationSettings.getMaxUploadSize(), is(this.maxUploadSize));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(applicationSettings, is(applicationSettings));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull" )
    public void shouldNotBeEqualToNull() {
        assertThat(applicationSettings.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(applicationSettings,
                   is(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings("String-c8a90a1d-4357-4486-846a-963795f4ff23" ,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings,
                   is(not(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.hashCode(),
                   is(new ApplicationSettings(applicationName,
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
        assertThat(applicationSettings.toString(), Matchers.startsWith("ApplicationSettings" ));
    }

}
