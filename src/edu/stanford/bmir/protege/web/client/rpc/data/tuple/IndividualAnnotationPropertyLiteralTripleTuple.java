package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.AnnotationProperty;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Literal;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.NamedIndividual;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualObject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class IndividualAnnotationPropertyLiteralTripleTuple extends TripleTuple<NamedIndividual, AnnotationProperty, Literal> {

    public IndividualAnnotationPropertyLiteralTripleTuple(VisualObject<NamedIndividual> subject, VisualObject<AnnotationProperty> property, VisualObject<Literal> literalVisualObject) {
        super(subject, property, literalVisualObject);
    }
}
