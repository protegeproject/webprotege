
package edu.stanford.bmir.protege.web.shared.app;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLocation_TestCase {

    private ApplicationLocation applicationLocation;

    private String scheme = "The scheme";

    private String host = "The host";

    private String path = "The path";

    private int port = 1;

    @Before
    public void setUp() {
        applicationLocation = new ApplicationLocation(scheme, host, path, port);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_scheme_IsNull() {
        new ApplicationLocation(null, host, path, port);
    }

    @Test
    public void shouldReturnSupplied_scheme() {
        assertThat(applicationLocation.getScheme(), is(this.scheme));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_host_IsNull() {
        new ApplicationLocation(scheme, null, path, port);
    }

    @Test
    public void shouldReturnSupplied_host() {
        assertThat(applicationLocation.getHost(), is(this.host));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_path_IsNull() {
        new ApplicationLocation(scheme, host, null, port);
    }

    @Test
    public void shouldReturnSupplied_path() {
        assertThat(applicationLocation.getPath(), is(this.path));
    }

    @Test
    public void shouldReturnSupplied_port() {
        assertThat(applicationLocation.getPort(), is(this.port));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(applicationLocation, is(applicationLocation));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(applicationLocation.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(applicationLocation, is(new ApplicationLocation(scheme, host, path, port)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_scheme() {
        assertThat(applicationLocation, is(not(new ApplicationLocation("String-d76cc1f8-72a8-4add-8d5f-05f13e927dde", host, path, port))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_host() {
        assertThat(applicationLocation, is(not(new ApplicationLocation(scheme, "String-023e1e8e-68c7-4cd7-aa19-6a4151fee5ce", path, port))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_path() {
        assertThat(applicationLocation, is(not(new ApplicationLocation(scheme, host, "String-ff99fbd1-4923-4b25-912c-c5cc16854b85", port))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_port() {
        assertThat(applicationLocation, is(not(new ApplicationLocation(scheme, host, path, 2))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(applicationLocation.hashCode(), is(new ApplicationLocation(scheme, host, path, port).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(applicationLocation.toString(), Matchers.startsWith("ApplicationLocation"));
    }

}
