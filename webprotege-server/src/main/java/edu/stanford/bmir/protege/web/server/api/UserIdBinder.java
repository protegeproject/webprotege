package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Apr 2018
 */
public class UserIdBinder extends AbstractBinder {

    @Inject
    public UserIdBinder() {
    }

    @Override
    protected void configure() {
        bindFactory(UserIdFactory.class).to(UserId.class)
                                        .proxy(false)
                                        .proxyForSameScope(false)
                                        .in(RequestScoped.class);
    }
}
