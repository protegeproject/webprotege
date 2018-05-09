package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKey;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 May 2018
 */
public class ApiKeyBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(ApiKeyFactory.class).to(ApiKey.class)
                                        .proxy(false)
                                        .proxyForSameScope(false)
                                        .in(RequestScoped.class);
    }
}
