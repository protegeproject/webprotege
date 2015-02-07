package edu.stanford.bmir.protege.web.server.dispatch;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DefaultActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DefaultDispatchServiceExecutor;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class DispatchModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ActionHandlerRegistry.class).to(DefaultActionHandlerRegistry.class).asEagerSingleton();
        bind(DispatchServiceHandler.class).to(DefaultDispatchServiceExecutor.class);
    }
}
