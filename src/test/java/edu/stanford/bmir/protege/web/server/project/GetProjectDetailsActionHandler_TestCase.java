
package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsAction;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.NullPointerException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetProjectDetailsActionHandler_TestCase {



    private GetProjectDetailsActionHandler handler;

    @Mock
    private ProjectDetailsManager projectDetailsManager;

    @Mock
    private GetProjectDetailsAction action;

    @Mock
    private GetProjectDetailsResult result;

    @Mock
    private ProjectId projectId;

    @Mock
    private ProjectDetails projectDetails;

    @Before
    public void setUp() {
        handler = new GetProjectDetailsActionHandler(projectDetailsManager);
        when(action.getProjectId()).thenReturn(projectId);
        when(projectDetailsManager.getProjectDetails(projectId)).thenReturn(projectDetails);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectDetailsManager_IsNull() {
        new GetProjectDetailsActionHandler(null);
    }

    @Test
    public void shouldImplementToString() {
        assertThat(handler.toString(), Matchers.startsWith("GetProjectDetailsActionHandler"));
    }

    @Test
    public void should_getRequestValidator() {
        assertThat(handler.getRequestValidator(action, mock(RequestContext.class)), is(NullValidator.get()));
    }

    @Test
    public void should_execute() {
        GetProjectDetailsResult res = handler.execute(action, mock(ExecutionContext.class));
        verify(projectDetailsManager).getProjectDetails(projectId);
        assertThat(res.getProjectDetails(), is(projectDetails));
    }

}
