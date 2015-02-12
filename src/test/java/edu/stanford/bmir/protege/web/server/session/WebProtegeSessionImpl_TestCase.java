package edu.stanford.bmir.protege.web.server.session;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
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
    private T value;

    @Before
    public void setUp() throws Exception {
        session = new WebProtegeSessionImpl(httpSession);
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

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionOnSetNullAttribute() {
        session.setAttribute(null, value);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionOnSetNullValue() {
        session.setAttribute(attribute, null);
    }

    @Test
    public void shouldReturnAbsentForAbsentValue() {
        Optional<T> value = session.getAttribute(attribute);
        assertThat(value.isPresent(), is(false));
    }

    @Test
    public void shouldReturnValueIfValueExists() {
        when(httpSession.getAttribute(ATTRIBUTE_NAME)).thenReturn(value);
        Optional<T> v = session.getAttribute(attribute);
        assertThat(v, is(Optional.of(value)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerOnRemoveNullAttribute() {
        session.removeAttribute(null);
    }

    @Test
    public void shouldRemoveAttribute() {
        session.removeAttribute(attribute);
        verify(httpSession, times(1)).removeAttribute(ATTRIBUTE_NAME);
    }

}