package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class ClsDataPropertyDatatypeTripleTuple extends TripleTuple<Cls, DataProperty, Datatype> {

    public ClsDataPropertyDatatypeTripleTuple(VisualCls subject, VisualDataProperty property, VisualDatatype object) {
        super(subject, property, object);
    }
}
