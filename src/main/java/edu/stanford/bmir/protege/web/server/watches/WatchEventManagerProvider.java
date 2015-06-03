package edu.stanford.bmir.protege.web.server.watches;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class WatchEventManagerProvider implements Provider<WatchEventManager> {

    private Provider<WatchEventManagerImpl> implProvider;

    @Inject
    public WatchEventManagerProvider(Provider<WatchEventManagerImpl> implProvider) {
        this.implProvider = implProvider;
    }

    @Override
    public WatchEventManager get() {
        WatchEventManagerImpl watchEventManager = implProvider.get();
        watchEventManager.attach();
        return watchEventManager;
    }
}
