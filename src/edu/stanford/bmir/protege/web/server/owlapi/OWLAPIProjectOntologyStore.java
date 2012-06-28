package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2012
 */
public abstract class OWLAPIProjectOntologyStore {

    public abstract OWLOntology getRootOntology();

}
