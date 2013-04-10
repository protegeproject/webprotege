package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract class VisualEntity<E extends OWLEntity> extends VisualPrimitive<E> implements Serializable {

    protected VisualEntity() {
        super();
    }

    protected VisualEntity(E entity) {
        super(entity, getDefaultShortForm(entity));
    }
    
    protected VisualEntity(E entity, String browserText) {
        super(entity, browserText);
    }

    protected static String getDefaultShortForm(OWLEntity entity) {
        return entity.getIRI().toQuotedString();
    }

}
