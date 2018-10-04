package edu.stanford.bmir.protege.web.client.crud;

import edu.stanford.bmir.protege.web.client.app.ApplicationInitManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class EntityCrudKitManagerInitializationTask implements ApplicationInitManager.ApplicationInitializationTask {

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public EntityCrudKitManagerInitializationTask(@Nonnull DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public String getName() {
        return  "EntityCrudKitManager initialization";
    }

    @Override
    public void run(final ApplicationInitManager.ApplicationInitTaskCallback callback) {
        dispatchServiceManager.execute(new GetEntityCrudKitsAction(), result -> {
            EntityCrudKitManager.get().init(result.getKits());
            callback.taskComplete();
        });
    }

}

