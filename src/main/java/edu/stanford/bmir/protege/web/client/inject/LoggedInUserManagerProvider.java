package edu.stanford.bmir.protege.web.client.inject;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public class LoggedInUserManagerProvider implements Provider<LoggedInUserManager> {

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private LoggedInUserManager manager;

    @Inject
    public LoggedInUserManagerProvider(EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public LoggedInUserManager get() {
        if(manager == null) {
            manager = LoggedInUserManager.getAndRestoreFromServer(eventBus, dispatchServiceManager);
        }
        return manager;
    }
}
