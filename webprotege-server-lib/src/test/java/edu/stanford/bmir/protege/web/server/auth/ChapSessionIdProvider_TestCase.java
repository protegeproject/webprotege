package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChapSessionId;
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
public class ChapSessionIdProvider_TestCase {

    private ChapSessionIdProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new ChapSessionIdProvider();
    }

    @Test
    public void shouldGetFreshId() throws Exception {
        ChapSessionId id1 = provider.get();
        ChapSessionId id2 = provider.get();
        assertThat(id1, is(not(id2)));
    }

    @Test
    public void shouldGetFreshIdOverDifferentProviders() {
        ChapSessionId id1 = provider.get();
        ChapSessionId id2 = new ChapSessionIdProvider().get();
        assertThat(id1, is(not(id2)));
    }
}
