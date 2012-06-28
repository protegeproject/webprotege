package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract class VisualObject<O extends Serializable> implements Serializable, Comparable<VisualObject> {

    private O object;
    
    private String browserText;

    protected VisualObject() {

    }

    public VisualObject(O object, String browserText) {
        if(object == null) {
            throw new NullPointerException("object must not be null.");
        }
        this.object = object;
        if(browserText == null) {
            throw new NullPointerException("browserText must not be null.");
        }
        this.browserText = browserText;
    }

    public String getBrowserText() {
        return browserText;
    }
    
    public O getObject() {
        return object;
    }

    public int compareTo(VisualObject o) {
        return browserText.compareTo(o.browserText);
    }
}
