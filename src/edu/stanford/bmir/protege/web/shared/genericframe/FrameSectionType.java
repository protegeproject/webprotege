package edu.stanford.bmir.protege.web.shared.genericframe;

import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/02/2013
 */
public class FrameSectionType<T> {

    public static FrameSectionType<String> JAVA_STRING;

    public static FrameSectionType<OWLLiteral> ANY_LITERAL;

    public static FrameSectionType<OWLLiteral> PLAIN_LITERAL_NO_LANG;

    public static FrameSectionType<OWLLiteral> PLAIN_LITERAL_WITH_LANG;

    public static FrameSectionType<OWLLiteral> INTEGER_LITERAL;

    public static FrameSectionType<OWLLiteral> DOUBLE_LITERAL;

    public static FrameSectionType<OWLLiteral> FLOAT_LITERAL;

    public static FrameSectionType<OWLLiteral> BOOLEAN_LITERAL;

    public static FrameSectionType<OWLLiteral> DATE_TIME_LITERAL;

    public static FrameSectionType<org.semanticweb.owlapi.model.IRI> IRI;

    public static FrameSectionType<OWLClass> CLASS;

    public static FrameSectionType<OWLObjectProperty> OBJECT_PROPERTY;

    public static FrameSectionType<OWLDataProperty> DATA_PROPERTY;

    public static FrameSectionType<OWLAnnotationProperty> ANNOTATION_PROPERTY;

    public static FrameSectionType<OWLNamedIndividual> NAMED_INDIVIDUAL;

    public static FrameSectionType<OWLDatatype> DATATYPE;

    public static FrameSectionType<OWLClassExpression> CLASS_EXPRESSION;

    public static FrameSectionType<PropertyValue> PROPERTY_VALUE;



//    - String
//    - PrimitiveData
//    - ClassExpression
//    [ Ontology, Annotations ]
//            - OutgoingPropertyValue
//    - IncomingPropertyValue
//
//    - ontologyList
}
