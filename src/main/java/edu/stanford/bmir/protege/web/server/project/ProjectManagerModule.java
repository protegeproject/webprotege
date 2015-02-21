package edu.stanford.bmir.protege.web.server.project;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class ProjectManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        // This is BAD. However, we need to replace instances of the manager with injection, so
        // we do this for now.
        bind(OWLAPIProjectManager.class).toInstance(OWLAPIProjectManager.getProjectManager());
    }
}
