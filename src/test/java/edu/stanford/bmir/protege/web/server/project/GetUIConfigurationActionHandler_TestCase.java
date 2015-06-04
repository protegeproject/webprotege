package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.GetUIConfigurationAction;
import edu.stanford.bmir.protege.web.shared.project.GetUIConfigurationResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectNotRegisteredException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUIConfigurationActionHandler_TestCase {

    private GetUIConfigurationActionHandler handler;

    @Mock
    private OWLAPIProjectManager projectManager;

    @Mock
    private GetUIConfigurationAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private ProjectLayoutConfiguration configuration;

    @Mock
    private OWLAPIProject project;

    @Mock
    private UIConfigurationManager uiConfigurationManager;

    @Before
    public void setUp() throws Exception {
        handler = new GetUIConfigurationActionHandler(projectManager);
        when(project.getUiConfigurationManager()).thenReturn(uiConfigurationManager);
    }

    @Test
    public void shouldReturnConfiguration() {
        when(uiConfigurationManager.getProjectLayoutConfiguration(any(UserId.class))).thenReturn(configuration);
        GetUIConfigurationResult result = handler.execute(action, project, executionContext);
        assertThat(result.getConfiguration(), is(configuration));
    }

    @Test(expected = ProjectNotRegisteredException.class)
    public void shouldRethrowProjectNotFoundException() {
        ProjectId projectId = mock(ProjectId.class);
        when(uiConfigurationManager.getProjectLayoutConfiguration(any(UserId.class)))
                .thenThrow(new ProjectNotRegisteredException(projectId));
        handler.execute(action, project, executionContext);
    }
}
