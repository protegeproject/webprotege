package edu.stanford.bmir.protege.web.client.rpc.data.primitive;


import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualObjectProperty extends VisualEntity<OWLObjectProperty> implements Serializable {

    public VisualObjectProperty() {
        super();
    }

    public VisualObjectProperty(OWLObjectProperty entity, String browserText) {
        super(entity, browserText);
    }

}
