package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@RunWith(MockitoJUnitRunner.class)
public class SetProjectSettingsActionHandler_TestCase {

    @Mock
    private ProjectDetailsManager psm;

    @Mock
    private SetProjectSettingsAction action;

    @Mock
    private ProjectSettings projectSettings;

    @Mock
    private ProjectId projectId;

    private SetProjectSettingsActionHandler handler;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private AccessManager accessManager;

    @Mock
    private ProjectDetailsManager detailsManager;

    @Before
    public void setUp() throws Exception {
        when(psm.getProjectSettings(projectId)).thenReturn(projectSettings);
        when(projectSettings.getProjectId()).thenReturn(projectId);
        when(action.getProjectId()).thenReturn(projectId);
        when(action.getProjectSettings()).thenReturn(projectSettings);
        handler = new SetProjectSettingsActionHandler(accessManager, detailsManager);
    }

    @Test
    public void shouldSetProjectSettings() {
        handler.execute(action, mock(ExecutionContext.class));
        verify(psm, times(1)).setProjectSettings(projectSettings);
    }
}
