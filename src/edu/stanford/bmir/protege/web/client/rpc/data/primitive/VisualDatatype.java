package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import org.semanticweb.owlapi.model.OWLDatatype;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualDatatype extends VisualEntity<OWLDatatype>  {

    public VisualDatatype(OWLDatatype entity, String browserText) {
        super(entity, browserText);
    }
}
