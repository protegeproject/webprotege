
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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationSettings_TestCase {

    private ApplicationSettings applicationSettings;

    private String applicationName = "The applicationName";

    private String customLogoUrl = "The customLogoUrl";

    private String adminEmailAddress = "The adminEmailAddress";

    @Mock
    private ApplicationLocation applicationLocation;

    @Before
    public void setUp()
    {
        applicationSettings = new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationName_IsNull() {
        new ApplicationSettings(null, customLogoUrl, adminEmailAddress, applicationLocation);
    }

    @Test
    public void shouldReturnSupplied_applicationName() {
        assertThat(applicationSettings.getApplicationName(), is(this.applicationName));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_customLogoUrl_IsNull() {
        new ApplicationSettings(applicationName, null, adminEmailAddress, applicationLocation);
    }

    @Test
    public void shouldReturnSupplied_customLogoUrl() {
        assertThat(applicationSettings.getCustomLogoUrl(), is(this.customLogoUrl));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_adminEmailAddress_IsNull() {
        new ApplicationSettings(applicationName, customLogoUrl, null, applicationLocation);
    }

    @Test
    public void shouldReturnSupplied_adminEmailAddress() {
        assertThat(applicationSettings.getAdminEmailAddress(), is(this.adminEmailAddress));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_applicationLocation_IsNull() {
        new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, null);
    }

    @Test
    public void shouldReturnSupplied_applicationLocation() {
        assertThat(applicationSettings.getApplicationLocation(), is(this.applicationLocation));
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
        assertThat(applicationSettings, is(new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_applicationName() {
        assertThat(applicationSettings, is(not(new ApplicationSettings("String-98073672-bb93-495a-915d-f58d37884e9b", customLogoUrl, adminEmailAddress, applicationLocation))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_customLogoUrl() {
        assertThat(applicationSettings, is(not(new ApplicationSettings(applicationName, "String-0f8ae6e4-d4ae-4214-862f-8de4cc1f5ffc", adminEmailAddress, applicationLocation))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_adminEmailAddress() {
        assertThat(applicationSettings, is(not(new ApplicationSettings(applicationName, customLogoUrl, "String-d412217f-ee0a-4337-8011-d749063e5d00", applicationLocation))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_applicationLocation() {
        assertThat(applicationSettings, is(not(new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, Mockito.mock(ApplicationLocation.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(applicationSettings.hashCode(), is(new ApplicationSettings(applicationName, customLogoUrl, adminEmailAddress, applicationLocation).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(applicationSettings.toString(), startsWith("ApplicationSettings"));
    }

}
