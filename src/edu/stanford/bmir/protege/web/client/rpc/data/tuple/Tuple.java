package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract class Tuple implements Serializable  {

    protected Tuple() {
    }

    public abstract int getSize();
}
