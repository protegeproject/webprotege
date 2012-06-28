package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract class Property extends Entity implements Serializable {

    protected Property() {
    }

    public Property(WebProtegeIRI iri) {
        super(iri);
    }
}
