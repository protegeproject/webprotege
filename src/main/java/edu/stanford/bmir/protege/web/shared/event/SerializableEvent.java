package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.web.bindery.event.shared.Event;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2013
 */
public abstract class SerializableEvent<H> extends Event<H> implements IsSerializable {

    private Serializable source;

    protected SerializableEvent(Serializable source) {
        this.source = source;
        setSource(source);
    }

    protected SerializableEvent() {
    }

    @Override
    protected void setSource(Object source) {
        if (source instanceof Serializable) {
            this.source = (Serializable) source;
        }
        super.setSource(source);
    }

    @Override
    public Object getSource() {
        if(this.source == null) {
            return super.getSource();
        }
        else {
            return this.source;
        }
    }
}
