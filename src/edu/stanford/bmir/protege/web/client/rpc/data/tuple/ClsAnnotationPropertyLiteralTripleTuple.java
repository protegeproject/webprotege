package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class ClsAnnotationPropertyLiteralTripleTuple extends TripleTuple<Cls, AnnotationProperty, Literal> {

    public ClsAnnotationPropertyLiteralTripleTuple(VisualObject<Cls> subject, VisualObject<AnnotationProperty> property, VisualObject<Literal> literalVisualObject) {
        super(subject, property, literalVisualObject);
    }

    public ClsAnnotationPropertyLiteralTripleTuple(VisualCls subject, VisualAnnotationProperty property, VisualLiteral object) {
        super(subject, property, object);
    }
}
