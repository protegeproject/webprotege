package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.ApplicationInitManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class EntityCrudKitManagerInitializationTask implements ApplicationInitManager.ApplicationInitializationTask {

    @Override
    public String getName() {
        return  "EntityCrudKitManager initialization";
    }

    @Override
    public void run(final ApplicationInitManager.ApplicationInitTaskCallback callback) {
        DispatchServiceManager.get().execute(new GetEntityCrudKitsAction(), new DispatchServiceCallback<GetEntityCrudKitsResult>() {
            @Override
            public void handleSuccess(GetEntityCrudKitsResult result) {
                EntityCrudKitManager.get().init(result.getKits());
                callback.taskComplete();
            }
        });
    }

}

