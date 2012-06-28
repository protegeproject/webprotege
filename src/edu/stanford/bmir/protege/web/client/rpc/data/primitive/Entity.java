package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract class Entity extends Primitive implements Serializable {

    private WebProtegeIRI iri;

    protected Entity() {
    }

    public Entity(WebProtegeIRI iri) {
        this.iri = iri;
    }

    public WebProtegeIRI getIRI() {
        return iri;
    }

    public abstract <R, E extends Exception> R accept(EntityVisitor<R, E> visitor) throws E;
}
