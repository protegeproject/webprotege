package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage;
import edu.stanford.bmir.protege.web.shared.auth.ChapSession;
import edu.stanford.bmir.protege.web.shared.auth.ChapSessionId;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Provider;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@RunWith(MockitoJUnitRunner.class)
public class ChapSessionFactory_TestCase {

    private ChapSessionFactory factory;

    @Mock
    private Provider<ChapSessionId> idProvider;

    @Mock
    private Provider<ChallengeMessage> challengeMessageProvider;

    @Mock
    private Salt salt;

    @Mock
    private ChapSessionId chapSessionId;

    @Mock
    private ChallengeMessage challengeMessage;

    @Before
    public void setUp() throws Exception {
        factory = new ChapSessionFactory(idProvider, challengeMessageProvider);
        when(idProvider.get()).thenReturn(chapSessionId);
        when(challengeMessageProvider.get()).thenReturn(challengeMessage);
    }

    @Test
    public void shouldAskForNewChapSessionId() {
        factory.create(salt);
        verify(idProvider, times(1)).get();
    }

    @Test
    public void shouldAskForNewChallengeMessage() {
        factory.create(salt);
        verify(challengeMessageProvider, times(1)).get();
    }

    @Test
    public void shouldUseProvidedSalt() {
        ChapSession session = factory.create(salt);
        assertThat(session.getSalt(), is(salt));
    }

    @Test
    public void shouldUseProvidedSessionId() {
        ChapSession session = factory.create(salt);
        assertThat(session.getId(), is(chapSessionId));
    }

    @Test
    public void shouldUseProvidedChallengeMessage() {
        ChapSession session = factory.create(salt);
        assertThat(session.getChallengeMessage(), is(challengeMessage));
    }
}
