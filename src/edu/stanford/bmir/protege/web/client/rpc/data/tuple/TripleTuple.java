package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualObject;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract  class TripleTuple<S extends Serializable, P extends Serializable, O extends Serializable> extends Tuple implements Serializable {

    public static final int SIZE = 3;

    private VisualObject<S> subject;
    
    private VisualObject<P> property;
    
    private VisualObject<O> object;

    protected TripleTuple() {

    }

    public TripleTuple(VisualObject<S> subject, VisualObject<P> property, VisualObject<O> object) {
        this.subject = subject;
        this.property = property;
        this.object = object;
    }

    public VisualObject<S> getVisualSubject() {
        return subject;
    }

    public VisualObject<P> getVisualProperty() {
        return property;
    }

    public VisualObject<O> getVisualObject() {
        return object;
    }

    public S getSubject() {
        return subject.getObject();
    }

    public P getProperty() {
        return property.getObject();
    }

    public O getObject() {
        return object.getObject();
    }

    @Override
    public int getSize() {
        return SIZE;
    }
}
