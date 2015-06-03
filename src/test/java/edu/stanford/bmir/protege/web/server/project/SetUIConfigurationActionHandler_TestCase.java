package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.SetUIConfigurationAction;
import edu.stanford.bmir.protege.web.shared.project.SetUIConfigurationActionHandler;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class SetUIConfigurationActionHandler_TestCase {

    private SetUIConfigurationActionHandler handler;

    @Mock
    private UIConfigurationManager uiConfigurationManager;

    @Mock
    private SetUIConfigurationAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private ProjectLayoutConfiguration configuration;

    @Mock
    private ProjectId projectId;

    @Mock
    private UserId userId;

    @Before
    public void setUp() throws Exception {
        handler = new SetUIConfigurationActionHandler(mock(OWLAPIProjectManager.class));
        when(action.getConfiguration()).thenReturn(configuration);
        when(action.getProjectId()).thenReturn(projectId);
        when(executionContext.getUserId()).thenReturn(userId);
    }

    @Test
    public void shouldSaveConfiguration() {
        handler.execute(action, executionContext);
        verify(uiConfigurationManager, times(1)).saveProjectLayoutConfiguration(projectId, userId, configuration);
    }

    @Test(expected = NotSignedInException.class)
    public void shouldNotSaveConfigurationForGuestUser() {
        when(userId.isGuest()).thenReturn(true);
        handler.execute(action, executionContext);
    }
}
