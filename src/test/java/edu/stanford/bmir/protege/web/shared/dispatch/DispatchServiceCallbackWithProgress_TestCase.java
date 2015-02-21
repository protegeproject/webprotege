package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.InvocationException;
import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class DispatchServiceCallbackWithProgress_TestCase<T> {


    public static final String PROGRESS_DISPLAY_TITLE = "ProgressDisplayTitle";
    public static final String PROGRESS_DISPLAY_MESSAGE = "Progress Display Message";
    private DispatchErrorMessageDisplay messageDisplay;

    private ProgressDisplay progressDisplay;

    private DispatchServiceCallbackWithProgressDisplay<T> callback;

    @Mock
    private T value;

    @Before
    public void setUp() throws Exception {
        messageDisplay = mock(DispatchErrorMessageDisplay.class);
        progressDisplay = mock(ProgressDisplay.class);
        callback = spy(new DispatchServiceCallbackWithProgressDisplay<T>(messageDisplay, progressDisplay) {
            @Override
            public String getProgressDisplayTitle() {
                return PROGRESS_DISPLAY_TITLE;
            }

            @Override
            public String getProgressDisplayMessage() {
                return PROGRESS_DISPLAY_MESSAGE;
            }
        });
    }

    @Test
    public void shouldShowProgressDisplay() {
        callback.handleSubmittedForExecution();
        verify(progressDisplay, times(1)).displayProgress(PROGRESS_DISPLAY_TITLE, PROGRESS_DISPLAY_MESSAGE);
    }

    @Test
    public void shouldHideProgressDisplay() {
        callback.handleFinally();
        verify(progressDisplay, times(1)).hideProgress();
    }

    @Test
    public void shouldCall_handleSuccess() {
        callback.onSuccess(value);
        verify(callback, times(1)).handleSuccess(value);
    }

    @Test
    public void shouldCall_handleExecutionException() {
        ActionExecutionException actionExecutionException = mock(ActionExecutionException.class);
        Throwable cause = mock(Throwable.class);
        when(actionExecutionException.getCause()).thenReturn(cause);
        callback.onFailure(actionExecutionException);
        verify(callback, times(1)).handleExecutionException(cause);
        verify(messageDisplay, times(1)).displayGeneralErrorMessage(any(String.class));
    }

    @Test
    public void shouldCall_handlePermissionDeniedException() {
        PermissionDeniedException exception = mock(PermissionDeniedException.class);
        callback.onFailure(exception);
        verify(callback, times(1)).handlePermissionDeniedException(exception);
        verify(messageDisplay, times(1)).displayPermissionDeniedErrorMessage();
    }

    @Test
    public void shouldDisplayInvocationExceptionMessage() {
        InvocationException exception = mock(InvocationException.class);
        callback.onFailure(exception);
        verify(messageDisplay, times(1)).displayInvocationExceptionErrorMessage();
    }

    @Test
    public void shouldDealWithOtherKindOfException() {
        Exception exception = mock(Exception.class);
        callback.onFailure(exception);
        verify(messageDisplay, times(1)).displayGeneralErrorMessage(anyString());
    }


    @Test
    public void shouldCallErrorFinallyOnError() {
        Exception ex = mock(Exception.class);
        callback.onFailure(ex);
        verify(callback, times(1)).handleErrorFinally(ex);
    }

    @Test
    public void shouldCallFinallyOnError() {
        callback.onFailure(mock(Exception.class));
        verify(callback, times(1)).handleFinally();
    }

    @Test
    public void shouldCallFinallyOnSuccess() {
        callback.onSuccess(value);
        verify(callback, times(1)).handleFinally();
    }

    @Test
    public void shouldNotCallErrorFinallyOnSuccess() {
        callback.onSuccess(value);
        verify(callback, never()).handleErrorFinally(any(Exception.class));
    }

}
