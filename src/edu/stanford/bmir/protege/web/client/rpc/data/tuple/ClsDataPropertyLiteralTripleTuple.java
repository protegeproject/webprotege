package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class ClsDataPropertyLiteralTripleTuple extends TripleTuple<Cls, DataProperty, Literal> {

    public ClsDataPropertyLiteralTripleTuple(VisualCls subject, VisualDataProperty property, VisualLiteral object) {
        super(subject, property, object);
    }
}
