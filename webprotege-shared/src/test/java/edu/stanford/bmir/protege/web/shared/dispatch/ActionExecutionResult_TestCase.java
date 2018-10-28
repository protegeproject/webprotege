package edu.stanford.bmir.protege.web.shared.dispatch;

import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Oct 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class ActionExecutionResult_TestCase {

    @Mock
    private DispatchServiceResultContainer resultContainer;

    @Mock
    private PermissionDeniedException permissionDeniedException;

    @Mock
    private ActionExecutionException actionExecutionException;

    @Test
    public void shouldCreateResultFromResult() {
        ActionExecutionResult result = ActionExecutionResult.get(resultContainer);
        assertThat(result.getResult(), is(Optional.of(resultContainer)));
        assertThat(result.getActionExecutionException().isPresent(), is(false));
        assertThat(result.getPermissionDeniedException().isPresent(), is(false));
    }

    @Test
    public void shouldCreateResultFromPermissionDeniedException() {
        ActionExecutionResult result = ActionExecutionResult.get(permissionDeniedException);
        assertThat(result.getPermissionDeniedException(), is(Optional.of(permissionDeniedException)));
        assertThat(result.getResult().isPresent(), is(false));
        assertThat(result.getActionExecutionException().isPresent(), is(false));
    }

    @Test
    public void shouldCreateResultFromActionExecutionException() {
        ActionExecutionResult result = ActionExecutionResult.get(actionExecutionException);
        assertThat(result.getActionExecutionException(), is(Optional.of(actionExecutionException)));
        assertThat(result.getResult().isPresent(), is(false));
        assertThat(result.getPermissionDeniedException().isPresent(), is(false));
    }
}
