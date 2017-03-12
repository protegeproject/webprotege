package edu.stanford.bmir.protege.web.server.hierarchy;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class ObjectPropertyHierarchyRootProvider implements Provider<OWLObjectProperty> {

    private final OWLDataFactory dataFactory;

    @Inject
    public ObjectPropertyHierarchyRootProvider(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @Override
    public OWLObjectProperty get() {
        return dataFactory.getOWLTopObjectProperty();
    }
}
