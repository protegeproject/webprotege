package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;
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

    public static final String DISPLAY_NAME = "MyDisplayName";

    public static final String DESCRIPTION = "MyDescription";
    public static final String PROJECT_TYPE_NAME = "MyType";

    private GetProjectSettingsActionHandler actionHandler;


    @Mock
    private ProjectId projectId;

    private OWLAPIProjectType projectType = new OWLAPIProjectType("MyType");

    @Mock
    private OWLAPIProjectMetadataManager mdm;

    @Mock
    private GetProjectSettingsAction action;

    @Mock
    private OWLAPIProject project;

    @Mock
    private ExecutionContext executionContext;

    @Before
    public void setUp() throws Exception {
        actionHandler = new GetProjectSettingsActionHandler(mdm);

        when(action.getProjectId()).thenReturn(projectId);

        when(mdm.getDisplayName(projectId)).thenReturn(DISPLAY_NAME);
        when(mdm.getDescription(projectId)).thenReturn(DESCRIPTION);
        when(mdm.getType(projectId)).thenReturn(projectType);

    }

    @Test
    public void shouldReturnSettings() {
        GetProjectSettingsResult result = actionHandler.execute(action, project, executionContext);
        assertThat(result.getProjectSettings().getProjectType().getName(), is(PROJECT_TYPE_NAME));
        assertThat(result.getProjectSettings().getProjectDescription(), is(DESCRIPTION));
    }
}
