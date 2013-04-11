package edu.stanford.bmir.protege.web.client.rpc.data.edge;

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

//    private Collection<OWLAnnotation> annotations;
//
//    private OWLProperty property;
//
//    private PropertyEdgeSubject subject;
//
//    private PropertyEdgeValue value;
//
//
//    private PropertyEdge(OWLObjectProperty property, OWLClass subject, OWLClass objectPropertyEdgeValue) {
//        this.property = property;
//        this.subject = subject;
//        this.value = objectPropertyEdgeValue;
//    }
//
//    private PropertyEdge(OWLDataProperty property, DataPropertyEdgeSubject subject, DataPropertyEdgeValue value) {
//        this.property = property;
//        this.subject = subject;
//        this.value = value;
//    }
//
//    private PropertyEdge(OWLAnnotationProperty property, AnnotationPropertyEdgeSubject subject, AnnotationPropertyEdgeValue value) {
//        this.property = property;
//        this.subject = subject;
//        this.value = value;
//    }
//
//
//    public OWLProperty getProperty() {
//        return property;
//    }
//
//    public PropertyEdgeSubject getSubject() {
//        return subject;
//    }
//
//    public PropertyEdgeValue getValue() {
//        return value;
//    }
//
//    public Collection<OWLAnnotation> getAnnotations() {
//        if(annotations == null) {
//            return Collections.emptySet();
//        }
//        else {
//            return new ArrayList<OWLAnnotation>(annotations);
//        }
//    }
//
//    public static PropertyEdge createObjectPropertyEdge(OWLObjectProperty property, OWLClass subject, OWLClass value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createObjectPropertyEdge(OWLObjectProperty property, OWLClass subject, OWLNamedIndividual value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createObjectPropertyEdge(OWLObjectProperty property, OWLNamedIndividual subject, OWLNamedIndividual value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createObjectPropertyEdge(OWLObjectProperty property, OWLNamedIndividual subject, OWLClass value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//
//
//    public static PropertyEdge createDataPropertyEdge(OWLDataProperty property, OWLClass subject, OWLDatatype value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createDataPropertyEdge(OWLDataProperty property, OWLClass subject, OWLLiteral value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createDataPropertyEdge(OWLDataProperty property, OWLNamedIndividual subject, OWLDatatype value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createDataPropertyEdge(OWLDataProperty property, OWLNamedIndividual subject, OWLLiteral value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//
//    public static PropertyEdge createAnnotationPropertyEdge(OWLAnnotationProperty property, IRI subject, IRI value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createAnnotationPropertyEdge(OWLAnnotationProperty property, IRI subject, OWLLiteral value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createAnnotationPropertyEdge(OWLAnnotationProperty property, IRI subject, AnonymousIndividual value) {
//        return new PropertyEdge(property, subject, value);
//    }
//
//    public static PropertyEdge createAnnotationPropertyEdge(OWLAnnotationProperty property, AnonymousIndividual subject, AnonymousIndividual value) {
//        return new PropertyEdge(property, subject, value);
//    }

}
