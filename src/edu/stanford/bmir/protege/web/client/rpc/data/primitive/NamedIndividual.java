package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class NamedIndividual extends Entity implements ObjectPropertyEdgeSubject, ObjectPropertyEdgeValue, DataPropertyEdgeSubject, Serializable {

    private NamedIndividual() {
    }

    public NamedIndividual(IRI iri) {
        super(iri);
    }

    @Override
    public <R, E extends Exception> R accept(EntityVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public VisualNamedIndividual toVisualObject(String browserText) {
        return new VisualNamedIndividual(this, browserText);
    }
}
