package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
public class DispatchServiceManagerProvider implements Provider<DispatchServiceManager> {

    @Override
    public DispatchServiceManager get() {
        return DispatchServiceManager.get();
    }
}
