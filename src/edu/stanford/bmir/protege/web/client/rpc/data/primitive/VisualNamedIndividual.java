package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualNamedIndividual extends VisualEntity<OWLNamedIndividual> implements Serializable {

    public VisualNamedIndividual() {
    }

    public VisualNamedIndividual(OWLNamedIndividual entity, String browserText) {
        super(entity, browserText);
    }

}
