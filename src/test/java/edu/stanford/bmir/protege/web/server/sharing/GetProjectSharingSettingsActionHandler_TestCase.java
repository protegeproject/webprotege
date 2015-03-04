package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsResult;
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
    private OWLAPIProject project;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private OWLAPIProjectManager projectManager;

    @Before
    public void setUp() throws Exception {
        handler = new GetProjectSharingSettingsActionHandler(settingsManager, projectManager);
        when(settingsManager.getProjectSharingSettings(projectId)).thenReturn(projectSharingSettings);
        when(action.getProjectId()).thenReturn(projectId);
    }

    @Test
    public void shouldReturnSharingSettings() {
        GetProjectSharingSettingsResult result = handler.execute(action, project, executionContext);
        assertThat(result.getProjectSharingSettings(), is(projectSharingSettings));
    }


}
