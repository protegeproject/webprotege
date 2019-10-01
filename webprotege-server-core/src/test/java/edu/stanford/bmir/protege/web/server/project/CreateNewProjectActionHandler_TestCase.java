package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.Resource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettingsManager;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.lang.DefaultDisplayNameSettingsFactory;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectAction;
import edu.stanford.bmir.protege.web.shared.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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

    private NewProjectSettings newProjectSettings;

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

    @Mock
    private UserInSessionFactory userInSessionFactory;

    @Mock
    private UserInSession userInSession;

    private DefaultDisplayNameSettingsFactory displayNameSettingsFactory;

    private String langTag = "en-GB";

    @Before
    public void setUp() throws Exception {
        displayNameSettingsFactory = new DefaultDisplayNameSettingsFactory();
        newProjectSettings = NewProjectSettings.get(UserId.getUserId("The Owner"),
                                                    "The display name",
                                                    langTag,
                                                    "The Project Description");
        handler = new CreateNewProjectActionHandler(projectManager,
                                                    projectDetailsManager,
                                                    accessManager,
                                                    userInSessionFactory);
        when(projectManager.createNewProject(this.newProjectSettings)).thenReturn(projectId);
        when(executionContext.getUserId()).thenReturn(userId);
        when(userInSessionFactory.getUserInSession(any())).thenReturn(userInSession);
        when(projectDetailsManager.getProjectDetails(projectId)).thenReturn(projectDetails);
        when(requestContext.getUserId()).thenReturn(userId);
        setPermission(true);

    }

    void setPermission(boolean allowed) {
        when(accessManager.hasPermission(
                Mockito.any(Subject.class),
                Mockito.any(Resource.class),
                Mockito.any(BuiltInAction.class))).thenReturn(allowed);
    }

    private void executeCreateNewProject() {
        handler.execute(new CreateNewProjectAction(newProjectSettings), executionContext);
    }

    @Test
    public void shouldCreateNewProject() throws Exception {
        setPermission(true);
        executeCreateNewProject();
        verify(projectManager, times(1)).createNewProject(newProjectSettings);
    }

    @Test(expected = PermissionDeniedException.class)
    public void shouldDenyCreateNewProject() throws Exception {
        setPermission(false);
        executeCreateNewProject();
    }

    @Test
    public void shouldRegisterNewProject() {
        executeCreateNewProject();
        verify(projectDetailsManager, times(1)).registerProject(projectId, newProjectSettings);
    }

    @Test(expected = PermissionDeniedException.class)
    public void shouldDenyRegisterNewProject() {
        setPermission(false);
        executeCreateNewProject();
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
