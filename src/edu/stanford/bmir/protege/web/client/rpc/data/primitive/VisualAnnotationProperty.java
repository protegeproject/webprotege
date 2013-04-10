package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualAnnotationProperty extends VisualEntity<OWLAnnotationProperty> implements Serializable {

    public VisualAnnotationProperty(OWLAnnotationProperty entity, String browserText) {
        super(entity, browserText);
    }
}
