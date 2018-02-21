package edu.stanford.bmir.protege.web.shared.issues;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public enum Status implements IsSerializable {

    OPEN,

    CLOSED;

    public boolean isOpen() {
        return this == OPEN;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}
