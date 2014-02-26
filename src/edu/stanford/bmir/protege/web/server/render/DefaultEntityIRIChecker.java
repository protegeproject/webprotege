package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Collection;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 20/02/2014
 */
public class DefaultEntityIRIChecker implements ManchesterSyntaxObjectRenderer.EntityIRIChecker {

    private OWLOntology rootOntology;

    public DefaultEntityIRIChecker(OWLOntology rootOntology) {
        this.rootOntology = rootOntology;
    }

    @Override
    public boolean isEntityIRI(IRI iri) {
        return iri.isTopEntity() || iri.isBottomEntity() || rootOntology.containsEntityInSignature(iri, true);
    }

    @Override
    public Collection<OWLEntity> getEntitiesWithIRI(IRI iri) {
        return rootOntology.getEntitiesInSignature(iri, true);
    }
}
