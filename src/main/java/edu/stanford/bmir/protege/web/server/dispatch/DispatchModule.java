package edu.stanford.bmir.protege.web.server.dispatch;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ActionHandlerRegistryImpl;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class DispatchModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ActionHandlerRegistry.class).to(ActionHandlerRegistryImpl.class).asEagerSingleton();
        bind(DispatchServiceExecutor.class).to(DispatchServiceExecutorImpl.class);
    }
}
