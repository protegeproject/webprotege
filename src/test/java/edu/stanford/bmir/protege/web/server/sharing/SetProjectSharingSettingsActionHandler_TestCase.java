package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsProjectOwnerValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
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
    private OWLAPIProject project;

    @Mock
    private OWLAPIProjectManager projectManager;

    @Mock
    private ValidatorFactory<UserIsProjectOwnerValidator> validatorFactory;

    @Before
    public void setUp() throws Exception {
        handler = new SetProjectSharingSettingsActionHandler(projectManager, sharingSettingsManager, validatorFactory);
        when(validatorFactory.getValidator(any(), any())).thenReturn(validator);
        when(action.getProjectSharingSettings()).thenReturn(sharingSettings);
    }

    @Test
    public void shouldReturnSuppliedValidator() {
        RequestValidator v = handler.getAdditionalRequestValidator(action, mock(RequestContext.class));
        assertThat(v, Matchers.<RequestValidator>is(validator));
    }

    @Test
    public void shouldSetSettings() {
        handler.execute(action, project, mock(ExecutionContext.class));
        verify(sharingSettingsManager, times(1)).setProjectSharingSettings(sharingSettings);
    }
}
