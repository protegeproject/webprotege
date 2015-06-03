package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.semanticweb.owlapi.model.HasContainsEntityInSignature;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class HasContainsEntityInSignatureImpl implements HasContainsEntityInSignature {

    private final OWLOntology rootOntology;

    @Inject
    public HasContainsEntityInSignatureImpl(@RootOntology OWLOntology rootOntology) {
        this.rootOntology = rootOntology;
    }

    @Override
    public boolean containsEntityInSignature(OWLEntity owlEntity) {
        return rootOntology.containsEntityInSignature(owlEntity, true);
    }
}
