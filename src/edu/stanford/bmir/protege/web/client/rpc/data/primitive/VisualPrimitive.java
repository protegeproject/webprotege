package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualPrimitive<O extends OWLObject> extends VisualObject<O> implements Serializable {

    protected VisualPrimitive() {
        super();
    }

    public VisualPrimitive(O object, String browserText) {
        super(object, browserText);
    }
}
