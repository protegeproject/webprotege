package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectSettingsActionHandler_TestCase {


    private GetProjectSettingsActionHandler actionHandler;


    @Mock
    private ProjectId projectId;

    @Mock
    private ProjectSettings projectSettings;

    @Mock
    private ProjectDetailsManager mdm;

    @Mock
    private GetProjectSettingsAction action;

    @Mock
    private OWLAPIProject project;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private OWLAPIProjectManager projectManager;

    @Before
    public void setUp() throws Exception {
        actionHandler = new GetProjectSettingsActionHandler(projectManager, mdm);

        when(action.getProjectId()).thenReturn(projectId);
        when(mdm.getProjectSettings(projectId)).thenReturn(projectSettings);

    }

    @Test
    public void shouldReturnSettings() {
        GetProjectSettingsResult result = actionHandler.execute(action, project, executionContext);
        assertThat(result.getProjectSettings(), is(projectSettings));
    }
}
