package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract class VisualEntity<E extends Entity> extends VisualPrimitive<E> implements Serializable {

    protected VisualEntity() {
        super();
    }

    protected VisualEntity(E entity) {
        super(entity, entity.getIRI().getShortForm());
    }
    
    protected VisualEntity(E entity, String browserText) {
        super(entity, browserText);
    }

}
