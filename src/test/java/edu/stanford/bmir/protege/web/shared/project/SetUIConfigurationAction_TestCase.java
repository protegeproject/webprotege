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
public class SetUIConfigurationAction_TestCase {


    private SetUIConfigurationAction action;

    private SetUIConfigurationAction otherAction;

    @Mock
    private ProjectId projectId;

    @Mock
    private ProjectLayoutConfiguration configuration;


    @Before
    public void setUp() throws Exception {
        action = new SetUIConfigurationAction(projectId, configuration);
        otherAction = new SetUIConfigurationAction(projectId, configuration);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectId_IsNull() {
        new SetUIConfigurationAction(null, configuration);
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Configuration_IsNull() {
        new SetUIConfigurationAction(projectId, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(equalTo(action)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(action, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(equalTo(otherAction)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(action.hashCode(), is(otherAction.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(action.toString(), startsWith("SetUIConfigurationAction"));
    }

    @Test
    public void shouldReturnSupplied_ProjectId() {
        assertThat(action.getProjectId(), is(projectId));
    }

    @Test
    public void shouldReturnSupplied_Configuration() {
        assertThat(action.getConfiguration(), is(configuration));
    }
}