package edu.stanford.bmir.protege.web.shared.app;

import edu.stanford.bmir.protege.web.client.app.ClientApplicationPropertiesDecoder;
import edu.stanford.bmir.protege.web.client.app.ClientObjectReader;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public class ClientApplicationPropertiesProvider implements Provider<ClientApplicationProperties> {

    @Inject
    public ClientApplicationPropertiesProvider() {
    }

    @Override
    public ClientApplicationProperties get() {
        return ClientObjectReader.create(
                "clientApplicationProperties", new ClientApplicationPropertiesDecoder()
        ).read();
    }
}
