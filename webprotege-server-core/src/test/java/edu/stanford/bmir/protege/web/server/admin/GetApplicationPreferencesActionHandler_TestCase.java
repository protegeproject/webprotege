
package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.app.ApplicationSettingsManager;
import edu.stanford.bmir.protege.web.server.app.GetApplicationSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_APPLICATION_SETTINGS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(value = org.mockito.junit.MockitoJUnitRunner.class)
public class GetApplicationPreferencesActionHandler_TestCase {

    private GetApplicationSettingsActionHandler handler;

    @Mock
    private AccessManager accessManager;

    @Mock
    private ApplicationSettingsManager applicationSettingsManager;

    @Mock
    private GetApplicationSettingsAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private UserId userId;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private RequestContext requestContext;

    @Mock
    private ApplicationSettings applicationSettings;

    public GetApplicationPreferencesActionHandler_TestCase() {
    }

    @Before
    public void setUp() throws Exception {
        handler = new GetApplicationSettingsActionHandler(accessManager, applicationSettingsManager);
        when(requestContext.getUserId()).thenReturn(userId);
        when(applicationSettingsManager.getApplicationSettings()).thenReturn(applicationSettings);
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
        GetApplicationSettingsResult result = handler.execute(action, executionContext);
        verify(applicationSettingsManager, times(1)).getApplicationSettings();
        assertThat(result.getApplicationSettings(), is(applicationSettings));
    }
}
