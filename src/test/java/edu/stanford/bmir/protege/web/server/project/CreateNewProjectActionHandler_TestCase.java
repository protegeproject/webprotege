package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateNewProjectActionHandler_TestCase {

    private CreateNewProjectActionHandler handler;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private ProjectDetailsManager projectDetailsManager;

    @Mock
    private ProjectSharingSettingsManager projectSharingSettingsManager;

    @Mock
    private NewProjectSettings newProjectSettings;

    @Mock
    private OWLAPIProject project;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private UserId userId;

    @Mock
    private ProjectId projectId;

    @Mock
    private ProjectDetails projectDetails;

    @Mock
    private RequestContext requestContext;

    @Mock
    private AccessManager accessManager;

    @Before
    public void setUp() throws Exception {
        handler = new CreateNewProjectActionHandler(projectManager, projectDetailsManager, projectSharingSettingsManager, accessManager);
        when(projectManager.createNewProject(newProjectSettings)).thenReturn(project);
        when(executionContext.getUserId()).thenReturn(userId);
        when(userId.getUserName()).thenReturn("User_Name");
        when(project.getProjectId()).thenReturn(projectId);
        when(projectDetailsManager.getProjectDetails(projectId)).thenReturn(projectDetails);
        when(requestContext.getUserId()).thenReturn(userId);

    }

    private void executeCreateNewProject() {
        handler.execute(new CreateNewProjectAction(newProjectSettings), executionContext);
    }

    @Test
    public void shouldCreateNewProject() throws Exception {
        executeCreateNewProject();
        verify(projectManager, times(1)).createNewProject(newProjectSettings);
    }

    @Test
    public void shouldRegisterNewProject() {
        executeCreateNewProject();
        verify(projectDetailsManager, times(1)).registerProject(projectId, newProjectSettings);
    }

    @Test
    public void shouldNotAllowGuestsToCreateProjects() {
        when(userId.isGuest()).thenReturn(true);
        CreateNewProjectAction action = new CreateNewProjectAction(newProjectSettings);
        RequestValidator validator = handler.getRequestValidator(action, requestContext);
        RequestValidationResult validationResult = validator.validateAction();
        assertThat(validationResult.isValid(), is(false));
    }
}
