package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
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
    private OWLAPIProject project;

    @Mock
    private ProjectId projectId;

    private SetProjectSettingsActionHandler handler;

    @Mock
    private OWLAPIProjectManager projectManager;

    @Before
    public void setUp() throws Exception {
        when(psm.getProjectSettings(projectId)).thenReturn(projectSettings);
        when(projectSettings.getProjectId()).thenReturn(projectId);
        when(action.getProjectId()).thenReturn(projectId);
        when(action.getProjectSettings()).thenReturn(projectSettings);
        handler = new SetProjectSettingsActionHandler(psm, projectManager);
    }

    @Test
    public void shouldSetProjectSettings() {
        handler.execute(action, project, mock(ExecutionContext.class));
        verify(psm, times(1)).setProjectSettings(projectSettings);
    }
}
