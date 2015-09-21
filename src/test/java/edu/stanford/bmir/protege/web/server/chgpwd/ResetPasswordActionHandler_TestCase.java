package edu.stanford.bmir.protege.web.server.chgpwd;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.metaproject.HasGetUserByUserIdOrEmail;
import edu.stanford.bmir.protege.web.server.metaproject.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordData;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordActionHandler_TestCase {

    private final String EMAIL_ADDRESS = "EMAIL.address";

    @Mock
    private HasGetUserByUserIdOrEmail mpm;

    @Mock
    private User user;

    @Mock
    private ResetPasswordMailer mailer;

    @Mock
    private ResetPasswordAction action;

    @Mock
    private ResetPasswordData data;

    @Mock
    private ExecutionContext context;

    private ResetPasswordActionHandler handler;

    @Mock
    private WebProtegeLogger logger;

    @Mock
    private UserId userId;

    @Before
    public void setUp() throws Exception {
        handler = new ResetPasswordActionHandler(mpm, mailer, logger);
        when(action.getResetPasswordData()).thenReturn(data);
        when(data.getEmailAddress()).thenReturn(EMAIL_ADDRESS);
        when(context.getUserId()).thenReturn(userId);
    }


    @Test
    public void shouldReturnInvalidEmailAddressIfCannotFindAnyUser() {
        when(mpm.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.<User>absent());
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnInvalidEmailAddressIfUserEmailAddressDoesNotExist() {
        when(mpm.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(null);
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnInvalidEmailAddressIfUserEmailAddressDoesEqualSuppliedEmailAddress() {
        when(mpm.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn("other.address");
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnSuccessIfEmailAddressComparesEqualIgnoreCase() {
        when(mpm.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn("email.address");
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.SUCCESS));
    }

    @Test
    public void shouldSendEmailOnSuccess() {
        when(mpm.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(EMAIL_ADDRESS);
        handler.execute(action, context);
        ArgumentCaptor<UserId> userIdCaptor = ArgumentCaptor.forClass(UserId.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailer, times(1)).sendEmail(userIdCaptor.capture(), emailCaptor.capture(), any(String.class));
        assertThat(userIdCaptor.getValue(), is(userId));
        assertThat(emailCaptor.getValue(), is(EMAIL_ADDRESS));
    }


    @Test
    public void shouldReturnErrorOnException() {
        when(mpm.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(EMAIL_ADDRESS);
        doThrow(new RuntimeException()).when(user).setPassword(any(String.class));
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INTERNAL_ERROR));
    }

    @Test
    public void shouldNotSendEmailOnException() {
        when(mpm.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(EMAIL_ADDRESS);
        doThrow(new RuntimeException()).when(user).setPassword(any(String.class));
        handler.execute(action, context);
        verify(mailer, never()).sendEmail(any(UserId.class), any(String.class), any(String.class));
    }
}
