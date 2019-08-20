package edu.stanford.bmir.protege.web.server.inject.project;

import org.semanticweb.owlapi.model.OWLDataFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@Singleton
public class OWLDataFactoryProvider implements Provider<OWLDataFactory> {

    @Override
    public OWLDataFactory get() {
        return new OWLDataFactoryImpl();
    }
}
