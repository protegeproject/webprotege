package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class VisualObjectProperty extends VisualEntity<ObjectProperty> implements Serializable {

    public VisualObjectProperty() {
        super();
    }

    public VisualObjectProperty(ObjectProperty entity, String browserText) {
        super(entity, browserText);
    }

}
