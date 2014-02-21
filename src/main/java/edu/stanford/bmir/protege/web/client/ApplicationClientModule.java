package edu.stanford.bmir.protege.web.client;

import com.google.gwt.inject.client.AbstractGinModule;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManagerProvider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
public class ApplicationClientModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(DispatchServiceManager.class).toProvider(DispatchServiceManagerProvider.class);
    }
}
