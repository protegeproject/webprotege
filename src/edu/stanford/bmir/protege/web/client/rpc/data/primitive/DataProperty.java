package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class DataProperty extends Property implements Serializable {

    private DataProperty() {

    }

    public DataProperty(IRI iri) {
        super(iri);
    }

    @Override
    public <R, E extends Exception> R accept(EntityVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public VisualDataProperty toVisualObject(String browserText) {
        return new VisualDataProperty(this, browserText);
    }
}
