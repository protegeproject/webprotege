package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
@ApplicationSingleton
public class ApplicationActionHandlerRegistry extends ActionHandlerRegistryImpl {

    @Inject
    public ApplicationActionHandlerRegistry(Set<ApplicationActionHandler> handlers) {
        super(handlers);
    }
}
