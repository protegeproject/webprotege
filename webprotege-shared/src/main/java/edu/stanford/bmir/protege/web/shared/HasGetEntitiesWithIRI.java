package edu.stanford.bmir.protege.web.shared;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public interface HasGetEntitiesWithIRI {

    Set<OWLEntity> getEntitiesWithIRI(IRI iri);
}
