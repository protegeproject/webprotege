package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsProjectOwnerValidator;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class SetProjectSharingSettingsActionHandler_TestCase {

    private SetProjectSharingSettingsActionHandler handler;

    @Mock
    private ProjectSharingSettingsManager sharingSettingsManager;

    @Mock
    private UserIsProjectOwnerValidator validator;

    @Mock
    private SetProjectSharingSettingsAction action;

    @Mock
    private ProjectSharingSettings sharingSettings;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private AccessManager accessManager;

    @Before
    public void setUp() throws Exception {
        handler = new SetProjectSharingSettingsActionHandler(accessManager, sharingSettingsManager);
//        when(validatorFactory.getValidator(any(), any())).thenReturn(validator);
        when(action.getProjectSharingSettings()).thenReturn(sharingSettings);
    }

    @Test
    public void shouldSetSettings() {
        handler.execute(action, mock(ExecutionContext.class));
        verify(sharingSettingsManager, times(1)).setProjectSharingSettings(sharingSettings);
    }
}
