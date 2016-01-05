package edu.stanford.bmir.protege.web.shared.app;

import edu.stanford.bmir.protege.web.client.app.ClientApplicationPropertiesDecoder;
import edu.stanford.bmir.protege.web.client.app.ClientObjectReader;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public class ClientApplicationPropertiesProvider implements Provider<ClientApplicationProperties> {

    @Override
    public ClientApplicationProperties get() {
        return ClientObjectReader.create(
                "clientApplicationProperties", new ClientApplicationPropertiesDecoder()
        ).read();
    }
}
