package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class AnnotationPropertyFrame implements EntityFrame<OWLAnnotationProperty>, HasPropertyValueList {

    private OWLAnnotationProperty subject;


    private PropertyValueList propertyValues;

    private Set<OWLEntity> domains;

    private Set<OWLEntity> ranges;

    private AnnotationPropertyFrame() {
    }

    public AnnotationPropertyFrame(OWLAnnotationProperty subject, Set<PropertyAnnotationValue> propertyValues, Set<OWLEntity> domains, Set<OWLEntity> ranges) {
        this.subject = subject;
        this.propertyValues = new PropertyValueList(propertyValues);
        this.domains = new HashSet<OWLEntity>(domains);
        this.ranges = new HashSet<OWLEntity>(ranges);
    }

    public OWLAnnotationProperty getSubject() {
        return subject;
    }

    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> sig = new HashSet<OWLEntity>();
        sig.add(subject);
        sig.addAll(propertyValues.getSignature());
        sig.addAll(domains);
        sig.addAll(ranges);
        return sig;
    }

    @Override
    public PropertyValueList getPropertyValueList() {
        return propertyValues;
    }

    public Set<PropertyAnnotationValue> getPropertyValues() {
        return propertyValues.getAnnotationPropertyValues();
    }

    public Set<OWLEntity> getDomains() {
        return new HashSet<OWLEntity>(domains);
    }

    public Set<OWLEntity> getRanges() {
        return new HashSet<OWLEntity>(ranges);
    }

    @Override
    public int hashCode() {
        return "AnnotationPropertyFrame".hashCode() + subject.hashCode() + propertyValues.hashCode() + domains.hashCode() + ranges.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnnotationPropertyFrame)) {
            return false;
        }
        AnnotationPropertyFrame other = (AnnotationPropertyFrame) obj;
        return this.subject.equals(other.subject) && this.propertyValues.equals(other.propertyValues) && this.domains.equals(other.propertyValues) && this.ranges.equals(other.ranges);
    }

    //    public Builder builder() {
//        return new Builder(subject, propertyValues);
//    }

//    public static class Builder {
//
//        private Set<PropertyAnnotationValue> propertyValues = new HashSet<PropertyAnnotationValue>();
//
//        private OWLAnnotationSubject subject;
//
//        public Builder(OWLAnnotationSubject subject, Set<PropertyAnnotationValue> propertyValues) {
//            this.subject = subject;
//            this.propertyValues.addAll(propertyValues);
//        }
//
//        public void addPropertyValue(OWLAnnotationProperty property, OWLAnnotationValue value) {
//            propertyValues.add(new PropertyAnnotationValue(property, value));
//        }
//
//        public void addPropertyValue(OWLAnnotationProperty property, String value) {
//            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
//        }
//
//        public void addPropertyValue(OWLAnnotationProperty property, int value) {
//            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
//        }
//
//        public void addPropertyValue(OWLAnnotationProperty property, double value) {
//            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
//        }
//
//        public AnnotationPropertyFrame build() {
//            return new AnnotationPropertyFrame(subject, propertyValues);
//        }
//
//    }

}
