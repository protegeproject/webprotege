package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectSharingSettingsActionHandler_TestCase {

    private GetProjectSharingSettingsActionHandler handler;

    @Mock
    private ProjectSharingSettingsManager settingsManager;

    @Mock
    private ProjectSharingSettings projectSharingSettings;

    @Mock
    private ProjectId projectId;

    @Mock
    private GetProjectSharingSettingsAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private AccessManager accessManager;

    @Before
    public void setUp() throws Exception {
        handler = new GetProjectSharingSettingsActionHandler(settingsManager, accessManager);
        when(settingsManager.getProjectSharingSettings(projectId)).thenReturn(projectSharingSettings);
        when(action.getProjectId()).thenReturn(projectId);
    }

    @Test
    public void shouldReturnSharingSettings() {
        GetProjectSharingSettingsResult result = handler.execute(action, executionContext);
        assertThat(result.getProjectSharingSettings(), is(projectSharingSettings));
    }


}
