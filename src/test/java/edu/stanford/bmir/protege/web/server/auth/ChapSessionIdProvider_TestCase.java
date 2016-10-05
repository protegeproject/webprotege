package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChapSessionId;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class ChapSessionIdProvider_TestCase {

    private ChapSessionIdProvider provider;

    @Before
    public void setUp() {
        provider = new ChapSessionIdProvider();
    }

    @Test
    public void shouldProvideFreshId() {
        ChapSessionId id1 = provider.get();
        ChapSessionId id2 = provider.get();
        assertThat(id1, is(not(equalTo(id2))));
    }
}
