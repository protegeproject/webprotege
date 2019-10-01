package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChapSession;
import edu.stanford.bmir.protege.web.shared.auth.ChapSessionId;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ChapSessionManager_TestCase {

    public static final long MAX_SESSION_DURATION = 1000;

    @Mock
    private ChapSessionFactory chapSessionFactory;

    @Mock
    private Salt salt;

    @Mock
    private ChapSession chapSession;

    @Mock
    private ChapSessionId sessionId;

    private ChapSessionManager manager;



    @Before
    public void setUp() throws Exception {
        when(chapSession.getId()).thenReturn(sessionId);
        manager = new ChapSessionManager(chapSessionFactory, MAX_SESSION_DURATION);
        when(chapSessionFactory.create(salt)).thenReturn(chapSession);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Salt_IsNull() {
        manager.getSession(null);
    }

    @Test
    public void shouldGetChapDataFromFactory() {
        manager.getSession(salt);
        verify(chapSessionFactory, times(1)).create(salt);
    }

    @Test
    public void shouldReturnChapDataFromFactory() {
        assertThat(manager.getSession(salt), is(chapSession));
    }


}
