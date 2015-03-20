package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 27/01/15
*/
public interface EntityIRIChecker {

    boolean isEntityIRI(IRI iri);

    Collection<OWLEntity> getEntitiesWithIRI(IRI iri);
}
