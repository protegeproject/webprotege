package edu.stanford.bmir.protege.web.shared.projectsettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@RunWith(MockitoJUnitRunner.class)
public class SetProjectSettingsResult_TestCase {

    @Mock
    private ProjectSettings settings;

    private SetProjectSettingsResult result;

    @Before
    public void setUp() throws Exception {
        result = new SetProjectSettingsResult(settings);
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectSettings_IsNull() {
        new SetProjectSettingsResult(null);
    }

    @Test
    public void shouldReturnSupplied_ProjectSettings() {
        assertThat(result.getProjectSettings(), is(settings));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(equalTo(result)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(result, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        SetProjectSettingsResult other = new SetProjectSettingsResult(settings);
        assertThat(result, is(equalTo(other)));
    }

    @Test
    public void shouldHaveSameHashCode() {
        SetProjectSettingsResult other = new SetProjectSettingsResult(settings);
        assertThat(result.hashCode(), is(other.hashCode()));
    }
}
