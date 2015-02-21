package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUIConfigurationResult_TestCase {


    private GetUIConfigurationResult result;

    private GetUIConfigurationResult otherResult;

    @Mock
    private ProjectLayoutConfiguration configuration;


    @Before
    public void setUp() throws Exception {
        result = new GetUIConfigurationResult(configuration);
        otherResult = new GetUIConfigurationResult(configuration);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new GetUIConfigurationResult(null);
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
        assertThat(result, is(equalTo(otherResult)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(result.hashCode(), is(otherResult.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(result.toString(), startsWith("GetUIConfigurationResult"));
    }

    @Test
    public void shouldReturnSuppliedProjectUIConfiguration() {
        assertThat(result.getConfiguration(), is(configuration));
    }
}