package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChallengeMessage;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class ChallengeMessageProvider_TestCase {

    private ChallengeMessageProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new ChallengeMessageProvider();
    }

    @Test
    public void shouldGenerateFreshChallengeMessage() {
        ChallengeMessage m1 = provider.get();
        ChallengeMessage m2 = provider.get();
        assertThat(m1, is(not(equalTo(m2))));
    }
}
