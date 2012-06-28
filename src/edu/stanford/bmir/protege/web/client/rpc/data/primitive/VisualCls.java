package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualCls extends VisualEntity<Cls> implements Serializable {

    private VisualCls() {
        super();
    }

    public VisualCls(Cls entity) {
        super(entity);
    }

    public VisualCls(Cls entity, String browserText) {
        super(entity, browserText);
    }
}
