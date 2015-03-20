package edu.stanford.bmir.protege.web.server.project;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectFileStoreFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class ProjectManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OWLAPIProjectManager.class).asEagerSingleton();
        install(new FactoryModuleBuilder().build(OWLAPIProjectFileStoreFactory.class));
    }
}
