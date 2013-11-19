package edu.stanford.bmir.protege.web.shared;

import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public interface HasContainsEntityInSignature {

    boolean containsEntityInSignature(IRI entityIRI);
}
