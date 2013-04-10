package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class AnnotationPropertyFrame {

    private OWLAnnotationSubject subject;


    private final Set<PropertyAnnotationValue> propertyValues;

    public AnnotationPropertyFrame(OWLAnnotationSubject subject, Set<PropertyAnnotationValue> propertyValues) {
        this.subject = subject;
        this.propertyValues = Collections.unmodifiableSet(new HashSet<PropertyAnnotationValue>(propertyValues));
    }

    public Set<PropertyAnnotationValue> getPropertyValues() {
        return new HashSet<PropertyAnnotationValue>(propertyValues);
    }

    public Builder builder() {
        return new Builder(subject, propertyValues);
    }

    public static class Builder {

        private Set<PropertyAnnotationValue> propertyValues = new HashSet<PropertyAnnotationValue>();

        private OWLAnnotationSubject subject;

        public Builder(OWLAnnotationSubject subject, Set<PropertyAnnotationValue> propertyValues) {
            this.subject = subject;
            this.propertyValues.addAll(propertyValues);
        }

        public void addPropertyValue(OWLAnnotationProperty property, OWLAnnotationValue value) {
            propertyValues.add(new PropertyAnnotationValue(property, value));
        }

        public void addPropertyValue(OWLAnnotationProperty property, String value) {
            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
        }

        public void addPropertyValue(OWLAnnotationProperty property, int value) {
            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
        }

        public void addPropertyValue(OWLAnnotationProperty property, double value) {
            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
        }

        public AnnotationPropertyFrame build() {
            return new AnnotationPropertyFrame(subject, propertyValues);
        }

    }

}
