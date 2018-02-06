package edu.stanford.bmir.protege.web.server.hierarchy;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class DataPropertyHierarchyRootProvider implements Provider<OWLDataProperty> {

    private final OWLDataFactory dataFactory;


    @Inject
    public DataPropertyHierarchyRootProvider(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @Override
    public OWLDataProperty get() {
        return dataFactory.getOWLTopDataProperty();
    }
}
