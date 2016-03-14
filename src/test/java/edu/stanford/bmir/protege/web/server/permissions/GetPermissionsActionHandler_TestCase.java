package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
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
public class GetPermissionsActionHandler_TestCase {

    private GetPermissionsActionHandler handler;

    @Mock
    private ProjectPermissionsManager permissionManager;

    @Mock
    private PermissionsSet permissionsSet;

    @Mock
    private UserId userId;

    @Mock
    private ProjectId projectId;

    @Mock
    private GetPermissionsAction action;

    @Before
    public void setUp() throws Exception {
        handler = new GetPermissionsActionHandler(permissionManager);
        when(permissionManager.getPermissionsSet(projectId, userId)).thenReturn(permissionsSet);

        when(action.getUserId()).thenReturn(userId);
        when(action.getProjectId()).thenReturn(projectId);
    }

    @Test
    public void shouldReturnActionClass() {
        Class<GetPermissionsAction> cls = handler.getActionClass();
        assertThat(cls, Matchers.<Class<GetPermissionsAction>>is(GetPermissionsAction.class));
    }

    @Test
    public void shouldGetPermissionsSet() {
        assertThat(handler.execute(action, mock(ExecutionContext.class)).getPermissionsSet(), is(permissionsSet));
    }

    @Test
    public void shouldAllowAnyOneToRetrievePermissions() {
        RequestContext requestContext = mock(RequestContext.class);
        when(requestContext.getUserId()).thenReturn(UserId.getGuest());
        RequestValidator<GetPermissionsAction> validator = handler.getRequestValidator(action, requestContext);
        assertThat(validator.validateAction(action, requestContext).isValid(), is(true));

    }
}
