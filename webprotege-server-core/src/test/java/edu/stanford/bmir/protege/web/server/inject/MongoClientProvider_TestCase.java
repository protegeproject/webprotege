package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MongoClientProvider_TestCase {

    private Optional<String> host;

    private Optional<Integer> port;

    private Optional<String> uri;

    @Mock
    private ApplicationDisposablesManager disposables;

    @Before
    public void setUp() throws Exception {
        host = Optional.empty();
        port = Optional.empty();
        uri = Optional.empty();

    }

    @Test
    public void shouldUseUriIfPresent() {
        uri = Optional.of("htp://10.11.12.13:9999");
        var clientProvider = new MongoClientProvider(host,
                                                      port,
                                                      uri,
                                                      Optional.empty(),
                                                      disposables);
        var client = clientProvider.get();
        assertThat(client.);
    }
}