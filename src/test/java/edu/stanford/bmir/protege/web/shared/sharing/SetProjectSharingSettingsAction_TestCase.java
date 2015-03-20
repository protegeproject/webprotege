package edu.stanford.bmir.protege.web.shared.sharing;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class SetProjectSharingSettingsAction_TestCase {


    private SetProjectSharingSettingsAction action;

    private SetProjectSharingSettingsAction otherAction;

    @Mock
    private ProjectSharingSettings projectSharingSettings;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() throws Exception {
        action = new SetProjectSharingSettingsAction(projectSharingSettings);
        otherAction = new SetProjectSharingSettingsAction(projectSharingSettings);
        when(projectSharingSettings.getProjectId()).thenReturn(projectId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new SetProjectSharingSettingsAction(null);
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
        assertThat(action.toString(), startsWith("SetProjectSharingSettingsAction"));
    }

    @Test
    public void shouldReturnSuppliedSharingSettings() {
        assertThat(action.getProjectSharingSettings(), is(projectSharingSettings));
    }

    @Test
    public void shouldReturnSuppliedProjectId() {
        assertThat(action.getProjectId(), is(projectId));
    }

}