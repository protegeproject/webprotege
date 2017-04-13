
package edu.stanford.bmir.protege.web.shared.app;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationSettings_TestCase {

    private ApplicationSettings applicationSettings;
    private String applicationName = "The applicationName";
    private String customLogoUrl = "The customLogoUrl";
    private String adminEmailAddress = "The adminEmailAddress";
    @Mock
    private ApplicationLocation applicationLocation;
    private long maxUploadSize = 1L;

    @Before
    public void setUp()
    {
        applicationSettings = new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, maxUploadSize);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationName_IsNull() {
        new ApplicationSettings(null, customLogoUrl, adminEmailAddress, applicationLocation, maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_applicationName() {
        assertThat(applicationSettings.getApplicationName(), is(this.applicationName));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_customLogoUrl_IsNull() {
        new ApplicationSettings(applicationName, null, adminEmailAddress, applicationLocation, maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_customLogoUrl() {
        assertThat(applicationSettings.getCustomLogoUrl(), is(this.customLogoUrl));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_adminEmailAddress_IsNull() {
        new ApplicationSettings(applicationName, customLogoUrl, null, applicationLocation, maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_adminEmailAddress() {
        assertThat(applicationSettings.getAdminEmailAddress(), is(this.adminEmailAddress));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationLocation_IsNull() {
        new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, null, maxUploadSize);
    }

    @Test
    public void shouldReturnSupplied_applicationLocation() {
        assertThat(applicationSettings.getApplicationLocation(), is(this.applicationLocation));
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
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(applicationSettings.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(applicationSettings, is(new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, maxUploadSize)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_applicationName() {
        assertThat(applicationSettings, is(Matchers.not(new ApplicationSettings("String-59344631-c3b4-4d9b-9091-e4cf31570afe", customLogoUrl, adminEmailAddress, applicationLocation, maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_customLogoUrl() {
        assertThat(applicationSettings, is(Matchers.not(new ApplicationSettings(applicationName, "String-9cdcccb5-5a5e-4191-8864-77c6257dde2a", adminEmailAddress, applicationLocation, maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_adminEmailAddress() {
        assertThat(applicationSettings, is(Matchers.not(new ApplicationSettings(applicationName, customLogoUrl, "String-05b955de-a3d7-4444-bef7-032fd22c0bb1", applicationLocation, maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_applicationLocation() {
        assertThat(applicationSettings, is(Matchers.not(new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, Mockito.mock(ApplicationLocation.class), maxUploadSize))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_maxUploadSize() {
        assertThat(applicationSettings, is(Matchers.not(new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, 2L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(applicationSettings.hashCode(), is(new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation, maxUploadSize).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(applicationSettings.toString(), Matchers.startsWith("ApplicationSettings"));
    }

}
