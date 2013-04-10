package edu.stanford.bmir.protege.web.shared;

import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/12/2012
 */
public interface HasLiterals {
    /**
     * Gets the literals contained in the object which implements this interface.
     * @return A set of literals.
     */
    Set<OWLLiteral> getLiterals();
}
