package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-21
 */
public class ApplicationExecutorsRegistry {

    @Nonnull
    private final ApplicationDisposablesManager disposablesManager;

    @Nonnull
    private final Set<ExecutorService> registeredServices = new HashSet<>();

    @Inject
    public ApplicationExecutorsRegistry(@Nonnull ApplicationDisposablesManager disposablesManager) {
        this.disposablesManager = checkNotNull(disposablesManager);
    }

    /**
     * Register an application executor service.  The service will be shutdown
     * at application shutdown.  Calling this method more than once will not cause
     * multiple registrations of the same executor service.
     * @param service The service
     * @param serviceName The name of the service e.g. "Download service"
     */
    public void registerService(@Nonnull ExecutorService service,
                                @Nonnull String serviceName) {
        if(registeredServices.contains(service)) {
            return;
        }
        registeredServices.add(service);
        disposablesManager.register(new ExecutorServiceShutdownTask(service, serviceName));
    }
}
