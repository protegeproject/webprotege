package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.metaproject.AuthenticationManager;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountAction;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateUserAccountActionHandler_TestCase {

    private CreateUserAccountActionHandler handler;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CreateUserAccountAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private UserId userId;

    @Mock
    private EmailAddress emailAddress;

    @Mock
    private SaltedPasswordDigest saltedPasswordDigest;

    @Mock
    private Salt salt;

    @Before
    public void setUp() throws Exception {
        handler = new CreateUserAccountActionHandler(authenticationManager);
        when(action.getUserId()).thenReturn(userId);
        when(action.getEmailAddress()).thenReturn(emailAddress);
        when(action.getPasswordDigest()).thenReturn(saltedPasswordDigest);
        when(action.getSalt()).thenReturn(salt);
    }

    @Test
    public void shouldCreateUserAccount() {
        handler.execute(action, executionContext);
        verify(authenticationManager, times(1)).registerUser(userId, emailAddress, saltedPasswordDigest, salt);
    }
}
