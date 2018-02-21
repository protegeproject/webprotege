package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class UserIsSignedInValidator_TestCase<A extends Action<?>> {

    private UserIsSignedInValidator validator;

    @Mock
    private A action;

    @Mock
    private RequestContext requestContext;

    @Mock
    private UserId userId;

    @Before
    public void setUp() throws Exception {
        validator = new UserIsSignedInValidator(userId);
        when(requestContext.getUserId()).thenReturn(userId);
    }

    @Test
    public void shouldValidateUserThatIsNotGuest() {
        when(userId.isGuest()).thenReturn(false);
        RequestValidationResult result = validator.validateAction();
        assertThat(result.isValid(), is(true));
    }

    @Test
    public void shouldNoValidateUserThatIsGuest() {
        when(userId.isGuest()).thenReturn(true);
        RequestValidationResult result = validator.validateAction();
        assertThat(result.isValid(), is(false));
    }
}
