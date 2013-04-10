package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import org.semanticweb.owlapi.model.OWLClass;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualNamedClass extends VisualEntity<OWLClass> implements Serializable {

    private VisualNamedClass() {
        super();
    }

    public VisualNamedClass(OWLClass entity) {
        super(entity);
    }

    public VisualNamedClass(OWLClass entity, String browserText) {
        super(entity, browserText);
    }
}
