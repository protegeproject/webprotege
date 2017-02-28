package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectPermissionsActionHandler_TestCase {

    private GetProjectPermissionsActionHandler handler;

    @Mock
    private ProjectPermissionsManager permissionManager;

    @Mock
    private AccessManager accessManager;

    @Mock
    private UserId userId;

    @Mock
    private ProjectId projectId;

    @Mock
    private GetProjectPermissionsAction action;


    @Before
    public void setUp() throws Exception {
        handler = new GetProjectPermissionsActionHandler(permissionManager, accessManager);
        when(action.getUserId()).thenReturn(userId);
        when(action.getProjectId()).thenReturn(projectId);
    }

    @Test
    public void shouldReturnActionClass() {
        Class<GetProjectPermissionsAction> cls = handler.getActionClass();
        assertThat(cls, Matchers.<Class<GetProjectPermissionsAction>>is(GetProjectPermissionsAction.class));
    }

    @Test
    public void shouldAllowAnyOneToRetrievePermissions() {
        RequestContext requestContext = mock(RequestContext.class);
        when(requestContext.getUserId()).thenReturn(UserId.getGuest());
        RequestValidator validator = handler.getRequestValidator(action, requestContext);
        assertThat(validator.validateAction().isValid(), is(true));

    }
}
