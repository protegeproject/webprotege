package edu.stanford.bmir.protege.web.server.project;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectCache;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectFileStoreFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@Module
public class ProjectManagerModule {

    @Provides
    @Singleton
    public OWLAPIProjectManager provideOWLAPIProjectManager(OWLAPIProjectCache projectCache) {
        return new OWLAPIProjectManager(projectCache);
    }

//    @Override
//    protected void configure() {
//        install(new FactoryModuleBuilder().build(OWLAPIProjectFileStoreFactory.class));
//    }
}
