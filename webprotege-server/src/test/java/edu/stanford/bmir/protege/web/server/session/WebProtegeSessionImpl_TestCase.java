package edu.stanford.bmir.protege.web.server.session;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeSessionImpl_TestCase<T> {


    public static final String ATTRIBUTE_NAME = "ATTRIBUTE_NAME";
    private WebProtegeSession session;


    @Mock
    private HttpSession httpSession;

    @Mock
    private WebProtegeSessionAttribute<T> attribute;

    @Mock
    private UserId userId;

    @Mock
    private T value;

    @Before
    public void setUp() throws Exception {
        session = new WebProtegeSessionImpl(userId);
        when(attribute.getAttributeName()).thenReturn(ATTRIBUTE_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new WebProtegeSessionImpl(null);
    }


    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(session, is(not(equalTo(null))));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(session.toString(), startsWith("WebProtegeSession"));
    }


    @Test
    public void shouldReturnGuestUser() {
        UserId userId = session.getUserInSession();
        assertThat(userId, is(UserId.getGuest()));
    }
    
    @Test
    public void shouldClearLoggedInUser() {
        session.clearUserInSession();
        verify(httpSession, times(1))
                .removeAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER.getAttributeName());
    }
}
