package edu.stanford.bmir.protege.web.server.shortform;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLDataFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-14
 */
@Module
public class DataFactoryModule {

    @Provides
    @ProjectSingleton
    OWLDataFactory provideOWLDataFactory() {
        return new OWLDataFactoryImpl();
    }


}
