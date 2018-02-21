package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.semanticweb.owlapi.model.HasGetEntitiesInSignature;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class HasGetEntitiesInSignatureImpl implements HasGetEntitiesInSignature {

    private final OWLOntology rootOntology;

    @Inject
    public HasGetEntitiesInSignatureImpl(@RootOntology OWLOntology rootOntology) {
        this.rootOntology = rootOntology;
    }

    @Override
    public Set<OWLEntity> getEntitiesInSignature(IRI entityIRI) {
        return rootOntology.getEntitiesInSignature(entityIRI, true);
    }
}
