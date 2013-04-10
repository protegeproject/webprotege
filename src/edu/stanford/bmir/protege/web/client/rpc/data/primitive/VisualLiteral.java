package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import org.semanticweb.owlapi.model.OWLLiteral;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualLiteral extends VisualPrimitive<OWLLiteral> implements Serializable {

    public VisualLiteral(OWLLiteral literal, String browserText) {
        super(literal, browserText);
    }

}
