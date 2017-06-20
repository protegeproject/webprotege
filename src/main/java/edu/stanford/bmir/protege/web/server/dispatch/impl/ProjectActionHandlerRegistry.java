package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
public class ProjectActionHandlerRegistry extends ActionHandlerRegistryImpl {

    @Inject
    public ProjectActionHandlerRegistry(Set<ProjectActionHandler> handlers) {
        super(handlers);
    }
}
