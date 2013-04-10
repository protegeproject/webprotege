package edu.stanford.bmir.protege.web.server.axiom;

import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
public interface AxiomRewriter {

    public Set<OWLAxiom> rewriteAxiom(OWLAxiom axiom);
}
