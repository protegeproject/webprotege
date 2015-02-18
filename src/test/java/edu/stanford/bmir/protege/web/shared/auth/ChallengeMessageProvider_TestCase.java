package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.server.auth.ChallengeMessageProvider;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/15
 */
public class ChallengeMessageProvider_TestCase {

    private ChallengeMessageProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new ChallengeMessageProvider();
    }

    @Test
    public void shouldReturnFreshChallengeMessage() {
        ChallengeMessage message1 = provider.get();
        ChallengeMessage message2 = provider.get();
        assertThat(message1, is(not(message2)));
    }
}
