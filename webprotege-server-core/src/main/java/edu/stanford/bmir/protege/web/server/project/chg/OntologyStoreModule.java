package edu.stanford.bmir.protege.web.server.project.chg;

import dagger.Module;
import dagger.Provides;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
@Module
public class OntologyStoreModule {

    @Provides
    OntologyStore provideOntologyStore(OntologyStoreImpl impl) {
        return impl;
    }

}
