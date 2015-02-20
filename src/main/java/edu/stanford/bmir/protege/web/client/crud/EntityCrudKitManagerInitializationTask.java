package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.ApplicationInitManager;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractDispatchServiceCallback;
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
        DispatchServiceManager.get().execute(new GetEntityCrudKitsAction(), new AbstractDispatchServiceCallback<GetEntityCrudKitsResult>() {
            @Override
            public void handleFinally() {
                callback.taskComplete();
            }

            @Override
            public void handleSuccess(GetEntityCrudKitsResult result) {
                GWT.log("Got EntityCrudKits");
                EntityCrudKitManager.get().init(result.getKits());
                callback.taskComplete();
            }
        });
    }

}

