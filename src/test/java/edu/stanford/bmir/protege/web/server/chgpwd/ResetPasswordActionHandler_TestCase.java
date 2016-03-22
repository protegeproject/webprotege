package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.auth.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.user.HasGetUserIdByUserIdOrEmail;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.auth.PasswordDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltProvider;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordData;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResultCode;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

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

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SaltProvider saltProvider;

    @Mock
    private PasswordDigestAlgorithm passwordDigestAlgorithm;

    @Mock
    private UserDetails userDetails;

    @Before
    public void setUp() throws Exception {
        handler = new ResetPasswordActionHandler(userDetailsManager,
                authenticationManager,
                saltProvider, passwordDigestAlgorithm, mailer, logger);

        when(action.getResetPasswordData()).thenReturn(data);
        when(data.getEmailAddress()).thenReturn(EMAIL_ADDRESS);
        when(context.getUserId()).thenReturn(userId);
    }


    @Test
    public void shouldReturnInvalidEmailAddressIfCannotFindAnyUser() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.<UserId>empty());
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnInvalidEmailAddressIfUserEmailAddressDoesNotExist() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(com.google.common.base.Optional.<String>absent());
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnInvalidEmailAddressIfUserEmailAddressDoesEqualSuppliedEmailAddress() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(com.google.common.base.Optional.of("other.address"));
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void shouldReturnSuccessIfEmailAddressComparesEqualIgnoreCase() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(com.google.common.base.Optional.of(EMAIL_ADDRESS));
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.SUCCESS));
    }

    @Test
    public void shouldSendEmailOnSuccess() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(com.google.common.base.Optional.of(EMAIL_ADDRESS));
        handler.execute(action, context);
        ArgumentCaptor<UserId> userIdCaptor = ArgumentCaptor.forClass(UserId.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailer, times(1)).sendEmail(userIdCaptor.capture(), emailCaptor.capture(), any(String.class));
        assertThat(userIdCaptor.getValue(), is(userId));
        assertThat(emailCaptor.getValue(), is(EMAIL_ADDRESS));
    }


    @Test
    public void shouldReturnErrorOnException() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(com.google.common.base.Optional.of(EMAIL_ADDRESS));
        doThrow(new RuntimeException()).when(authenticationManager).setDigestedPassword(
                any(UserId.class),
                any(SaltedPasswordDigest.class),
                any(Salt.class));
        ResetPasswordResult result = handler.execute(action, context);
        assertThat(result.getResultCode(), is(ResetPasswordResultCode.INTERNAL_ERROR));
    }

    @Test
    public void shouldNotSendEmailOnException() {
        when(userDetailsManager.getUserByUserIdOrEmail(any(String.class))).thenReturn(Optional.of(userId));
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(userDetails.getEmailAddress()).thenReturn(com.google.common.base.Optional.of(EMAIL_ADDRESS));
        doThrow(new RuntimeException()).when(authenticationManager).setDigestedPassword(
                any(UserId.class),
                any(SaltedPasswordDigest.class),
                any(Salt.class));
        handler.execute(action, context);
        verify(mailer, never()).sendEmail(any(UserId.class), any(String.class), any(String.class));
    }
}
