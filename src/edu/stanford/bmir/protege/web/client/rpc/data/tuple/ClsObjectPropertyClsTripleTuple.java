package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class ClsObjectPropertyClsTripleTuple extends TripleTuple<Cls, ObjectProperty, Cls> implements Serializable {

    private ClsObjectPropertyClsTripleTuple() {
    }

    public ClsObjectPropertyClsTripleTuple(VisualCls subject, VisualObjectProperty property, VisualCls object) {
        super(subject, property, object);
    }
}
