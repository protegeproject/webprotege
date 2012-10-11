package edu.stanford.bmir.protege.web.client.rpc.data.edge;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2012
 * <p>
 *    A PropertyEdge captures some kind of "relationship" (for a given object property, data property or annotation property)
 *    between two entities.  To be more precise, the notion of a relationship captured by a single PropertyEdge
 *    in terms of OWL constructs is defined as follows:
 *
 *    For an object property propP
 *
 *    ClassA to ClassB      := SubClassOf(ClassA ObjectSomeValuesFrom(propP ClassB))
 *
 *    ClassA to IndB        := SubClassOf(ClassA ObjectHasValue(propP IndB))
 *
 *    IndA   to ClassB      := ClassAssertion(ObjectSomeValuesFrom(propP ClassB) IndA)
 *
 *    IndA   to IndB        := ObjectPropertyAssertion(propP IndA IndB)
 *
 *
 *
 *    For a data property propP
 *
 *    ClassA to DatatypeB   := SubClassOf(ClassA DataSomeValuesFrom(propP DatatypeB))
 *
 *    ClassA to LiteralB    := SubClassOf(ClassA DataHasValue(propP LiteralB))
 *
 *    IndA   to DatatypeB   := ClassAssertion(DataSomeValuesFrom(propP DatatypeB) IndA)
 *
 *    IndA   to LiteralB    := DataPropertyAssertion(propP IndA LiteralB)
 *
 *
 *    For an annotation property propP
 *
 *    IRIA   to IRIB        := AnnotationAssertion(propP  IRIA IRIB)
 *
 *    IRIA   to AnonB       := AnnotationAssertion(propP IRIA AnonB)
 *
 *    IRIA   to LiteralB    := AnnotationAssertion(propP IRIA LiteralB)
 *
 *    AnonA  to IRIB        := AnnotationAssertion(propP AnonA IRIB)
 *
 *    AnonA  to AnonB       := AnnotationAssertion(propP AnonA AnonB)
 *
 *    AnonA  to LiteralB    := AnnotationAssertion(propP AnonA LiteralB)
 * </p>
 */
public class PropertyEdge {

    private Collection<Annotation> annotations;

    private Property property;

    private PropertyEdgeSubject subject;

    private PropertyEdgeValue value;


    private PropertyEdge(ObjectProperty property, ObjectPropertyEdgeSubject subject, ObjectPropertyEdgeValue objectPropertyEdgeValue) {
        this.property = property;
        this.subject = subject;
        this.value = objectPropertyEdgeValue;
    }

    private PropertyEdge(DataProperty property, DataPropertyEdgeSubject subject, DataPropertyEdgeValue value) {
        this.property = property;
        this.subject = subject;
        this.value = value;
    }

    private PropertyEdge(AnnotationProperty property, AnnotationPropertyEdgeSubject subject, AnnotationPropertyEdgeValue value) {
        this.property = property;
        this.subject = subject;
        this.value = value;
    }


    public Property getProperty() {
        return property;
    }

    public PropertyEdgeSubject getSubject() {
        return subject;
    }

    public PropertyEdgeValue getValue() {
        return value;
    }

    public Collection<Annotation> getAnnotations() {
        if(annotations == null) {
            return Collections.emptySet();
        }
        else {
            return new ArrayList<Annotation>(annotations);
        }
    }

    public static PropertyEdge createObjectPropertyEdge(ObjectProperty property, NamedClass subject, NamedClass value) {
        return new PropertyEdge(property, subject, value);
    }

    public static PropertyEdge createObjectPropertyEdge(ObjectProperty property, NamedClass subject, NamedIndividual value) {
        return new PropertyEdge(property, subject, value);
    }
    
    public static PropertyEdge createObjectPropertyEdge(ObjectProperty property, NamedIndividual subject, NamedIndividual value) {
        return new PropertyEdge(property, subject, value);
    }
    
    public static PropertyEdge createObjectPropertyEdge(ObjectProperty property, NamedIndividual subject, NamedClass value) {
        return new PropertyEdge(property, subject, value);
    }



    public static PropertyEdge createDataPropertyEdge(DataProperty property, NamedClass subject, Datatype value) {
        return new PropertyEdge(property, subject, value);
    }

    public static PropertyEdge createDataPropertyEdge(DataProperty property, NamedClass subject, Literal value) {
        return new PropertyEdge(property, subject, value);
    }

    public static PropertyEdge createDataPropertyEdge(DataProperty property, NamedIndividual subject, Datatype value) {
        return new PropertyEdge(property, subject, value);
    }

    public static PropertyEdge createDataPropertyEdge(DataProperty property, NamedIndividual subject, Literal value) {
        return new PropertyEdge(property, subject, value);
    }


    public static PropertyEdge createAnnotationPropertyEdge(AnnotationProperty property, IRI subject, IRI value) {
        return new PropertyEdge(property, subject, value);
    }

    public static PropertyEdge createAnnotationPropertyEdge(AnnotationProperty property, IRI subject, Literal value) {
        return new PropertyEdge(property, subject, value);
    }

    public static PropertyEdge createAnnotationPropertyEdge(AnnotationProperty property, IRI subject, AnonymousIndividual value) {
        return new PropertyEdge(property, subject, value);
    }

    public static PropertyEdge createAnnotationPropertyEdge(AnnotationProperty property, AnonymousIndividual subject, AnonymousIndividual value) {
        return new PropertyEdge(property, subject, value);
    }

}
