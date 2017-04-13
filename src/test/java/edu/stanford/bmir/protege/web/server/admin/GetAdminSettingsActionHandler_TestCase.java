
package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.admin.AdminSettings;
import edu.stanford.bmir.protege.web.shared.admin.GetAdminSettingsAction;
import edu.stanford.bmir.protege.web.shared.admin.GetAdminSettingsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_APPLICATION_SETTINGS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(value = org.mockito.runners.MockitoJUnitRunner.class)
public class GetAdminSettingsActionHandler_TestCase {

    private GetAdminSettingsActionHandler handler;

    @Mock
    private AccessManager accessManager;

    @Mock
    private AdminSettingsManager adminSettingsManager;

    @Mock
    private GetAdminSettingsAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private UserId userId;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private RequestContext requestContext;

    @Mock
    private AdminSettings adminSettings;

    public GetAdminSettingsActionHandler_TestCase() {
    }

    @Before
    public void setUp() throws Exception {
        handler = new GetAdminSettingsActionHandler(accessManager, adminSettingsManager);
        when(executionContext.getUserId()).thenReturn(userId);
        when(requestContext.getUserId()).thenReturn(userId);
        when(adminSettingsManager.getAdminSettings()).thenReturn(adminSettings);
    }

    @Test
    public void shouldCheckForPermission() {
        RequestValidator validator = handler.getRequestValidator(action, requestContext);
        RequestValidationResult result = validator.validateAction();
        assertThat(result.isInvalid(), is(true));
        verify(accessManager, times(1)).hasPermission(forUser(userId),
                                                      ApplicationResource.get(),
                                                      EDIT_APPLICATION_SETTINGS.getActionId());
    }

    @Test
    public void shouldGetAdminSettings() {
        GetAdminSettingsResult result = handler.execute(action, executionContext);
        verify(adminSettingsManager, times(1)).getAdminSettings();
        assertThat(result.getAdminSettings(), is(adminSettings));
    }
}
