package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class TripleTupleFactory {

    public static TripleTuple<Cls, ObjectProperty, Cls> createTripleTuple(VisualCls cls, VisualObjectProperty property, VisualCls value) {
        return new ClsObjectPropertyClsTripleTuple(cls, property, value);
    }

    public static TripleTuple<Cls, ObjectProperty, NamedIndividual> createTripleTuple(VisualCls cls, VisualObjectProperty property, VisualNamedIndividual value) {
        return new ClsObjectPropertyIndividualTripleTuple(cls, property, value);
    }

    public static TripleTuple<Cls, AnnotationProperty, Literal> createTripleTuple(VisualCls cls, VisualAnnotationProperty property, VisualLiteral value) {
        return new ClsAnnotationPropertyLiteralTripleTuple(cls, property, value);
    }

    public static TripleTuple createTripleTuple(VisualCls cls, VisualDataProperty property, VisualDatatype value) {
        return new ClsDataPropertyDatatypeTripleTuple(cls, property, value);
    }

    public static TripleTuple createTripleTuple(VisualCls cls, VisualDataProperty property, VisualLiteral value) {
        return new ClsDataPropertyLiteralTripleTuple(cls, property, value);
    }




    public static TripleTuple createTripleTuple(VisualNamedIndividual cls, VisualObjectProperty property, VisualCls value) {
        return new IndividualObjectPropertyClsTripleTuple(cls, property, value);
    }

    public static TripleTuple createTripleTuple(VisualNamedIndividual cls, VisualObjectProperty property, VisualNamedIndividual value) {
        return new IndividualObjectPropertyIndividualTripleTuple(cls, property, value);
    }

    public static TripleTuple createTripleTuple(VisualNamedIndividual cls, VisualAnnotationProperty property, VisualLiteral value) {
        return new IndividualAnnotationPropertyLiteralTripleTuple(cls, property, value);
    }

    public static TripleTuple createTripleTuple(VisualNamedIndividual cls, VisualDataProperty property, VisualDatatype value) {
        return new IndividualDataPropertyDatatypeTripleTuple(cls, property, value);
    }

    public static TripleTuple createTripleTuple(VisualNamedIndividual cls, VisualDataProperty property, VisualLiteral value) {
        return new IndividualDataPropertyLiteralTripleTuple(cls, property, value);
    }
}
