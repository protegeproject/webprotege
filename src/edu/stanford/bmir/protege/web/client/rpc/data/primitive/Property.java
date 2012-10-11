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

    protected Property(Prefix prefix, String localName) {
        super(prefix, localName);
    }

    public Property(IRI iri) {
        super(iri);
    }
}
