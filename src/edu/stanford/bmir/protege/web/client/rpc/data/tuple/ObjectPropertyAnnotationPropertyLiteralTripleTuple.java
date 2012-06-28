package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class ObjectPropertyAnnotationPropertyLiteralTripleTuple extends TripleTuple<ObjectProperty, AnnotationProperty, Literal>  {

    public ObjectPropertyAnnotationPropertyLiteralTripleTuple(VisualObjectProperty subject, VisualAnnotationProperty property, VisualLiteral object) {
        super(subject, property, object);
    }
}
