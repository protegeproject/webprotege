package edu.stanford.bmir.protege.web.client.rpc.data.tuple;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class TripleTupleFactory {

    public static TripleTuple<NamedClass, ObjectProperty, NamedClass> createTripleTuple(VisualNamedClass cls, VisualObjectProperty property, VisualNamedClass value) {
        return new ClsObjectPropertyClsTripleTuple(cls, property, value);
    }

    public static TripleTuple<NamedClass, ObjectProperty, NamedIndividual> createTripleTuple(VisualNamedClass cls, VisualObjectProperty property, VisualNamedIndividual value) {
        return new ClsObjectPropertyIndividualTripleTuple(cls, property, value);
    }

    public static TripleTuple<NamedClass, AnnotationProperty, Literal> createTripleTuple(VisualNamedClass cls, VisualAnnotationProperty property, VisualLiteral value) {
        return new ClsAnnotationPropertyLiteralTripleTuple(cls, property, value);
    }

    public static TripleTuple createTripleTuple(VisualNamedClass cls, VisualDataProperty property, VisualDatatype value) {
        return new ClsDataPropertyDatatypeTripleTuple(cls, property, value);
    }

    public static TripleTuple createTripleTuple(VisualNamedClass cls, VisualDataProperty property, VisualLiteral value) {
        return new ClsDataPropertyLiteralTripleTuple(cls, property, value);
    }




    public static TripleTuple createTripleTuple(VisualNamedIndividual cls, VisualObjectProperty property, VisualNamedClass value) {
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
